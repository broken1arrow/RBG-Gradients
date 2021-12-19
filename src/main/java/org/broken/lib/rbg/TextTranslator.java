package org.broken.lib.rbg;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.ChatColor;
import org.json.simple.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextTranslator {
	private static final Pattern HEX_PATTERN = Pattern.compile("(?<!\\\\\\\\)(<#[a-fA-F0-9]{6}>)");
	private static final Pattern GRADIENT_PATTERN = Pattern.compile("(<#[a-fA-F0-9]{6}:#[a-fA-F0-9]{6}>)");
	private static final List<String> SPECIAL_SIGN = Arrays.asList("&l", "&n", "&o", "&k", "&m");

	/**
	 * This is for compenent when you want to send message
	 * thru vanilla minecraft(MNS).
	 * DO NOT WORK IN SPIGOT API.Use {@link #toSpigotFormat(String)}
	 * <p>
	 * Type your message/string text here. you use
	 * <p>
	 * §/& colorcode or <#55F758> for normal hex and
	 * <#5e4fa2:#f79459> for gradient (use §/&r to stop gradient).
	 *
	 * @param message your string message.
	 * @return json to string.
	 */

	public static String toCompenent(String message) {
		return toCompenent(message, null);
	}

	/**
	 * This is for compenent when you want to send message
	 * thru vanilla minecraft(MNS).
	 * DO NOT WORK IN SPIGOT API. Use {@link #toSpigotFormat(String)}
	 * <p>
	 * Type your message/string text here. you use
	 * <p>
	 * §/& colorcode or <#55F758> for normal hex and
	 * <#5e4fa2:#f79459> for gradient (use §/&r to stop gradient).
	 *
	 * @param message      your string message.
	 * @param defaultColor set defult color when color are not set in message.
	 * @return json to string.
	 */

	public static String toCompenent(String message, String defaultColor) {
		JsonArray jsonarray = new JsonArray();
		List<Component> components = new ArrayList<>();
		Component.Builder compenent = new Component.Builder();
		Matcher matcherGradient = GRADIENT_PATTERN.matcher(message);
		if (defaultColor == null || defaultColor.equals(""))
			defaultColor = "white";

		if (matcherGradient.find()) {
			message = createGradient(message);
		}
		StringBuilder builder = new StringBuilder(message.length());
		Matcher matcher = HEX_PATTERN.matcher(message);

		if (matcher.find()) {
			matcher.reset();
			while (matcher.find()) {
				String match = matcher.group(0);
				message = message.replace(match, match.replace("<", "§"));
			}
		}
		for (int i = 0; i < message.length(); i++) {
			char letter = message.charAt(i);
			boolean checkChar;
			if (i + 1 < message.length() && letter == ChatColors.COLOR_CHAR || letter == '&') {
				char msg = message.charAt(i + 1);
				checkChar = checkIfColor(msg) || msg == '#';
			} else
				checkChar = false;

			//System.out.println("letter " + letter);
			if (checkChar) {
				if (++i >= message.length()) {
					break;
				}
				letter = message.charAt(i);

				if (letter >= 'A' && letter <= 'Z') {
					letter += 32;
				}
				String format;

				//System.out.println("letter inside " + letter);

				if (!checkIfColor(letter) && letter == '#') {
					StringBuilder hex = new StringBuilder();
					for (int j = 0; j < 7; j++) {
						hex.append(message.charAt(i + j));
					}
					if (!isValidHexaCode(hex.toString())) {
						System.out.println("this " + hex + " are not a hex color");
						continue;
					}
					format = hex.toString();
					i += 7;
				} else {
					try {
						format = ChatColors.getByChar(letter).getName();
					} catch (Exception ignore) {
						format = null;
					}
				}
				if (format == null) {
					System.out.println("foramt nulllll");
					continue;
				}
				if (builder.length() > 0) {

					//System.out.println("builder oute " + builder.toString());
					compenent.message(builder.toString());
					builder = new StringBuilder();
					jsonarray.add(compenent.build().toString());
					components.add(compenent.build());
					compenent = new Component.Builder();

				}
				if (format.equals(ChatColors.BOLD.getName())) {
					compenent.bold(true);
				} else if (format.equals(ChatColors.ITALIC.getName())) {
					compenent.italic(true);
				} else if (format.equals(ChatColors.UNDERLINE.getName())) {
					compenent.underline(true);
				} else if (format.equals(ChatColors.STRIKETHROUGH.getName())) {
					compenent.strikethrough(true);
				} else if (format.equals(ChatColors.MAGIC.getName())) {
					compenent.obfuscated(true);
				} else if (format.equals(ChatColors.RESET.getName())) {
					format = defaultColor;
					compenent.reset(true);
					compenent.colorCode(format);
				} else {
					compenent.colorCode(format);
				}
				continue;
			}
			builder.append(letter);
		}

		compenent.message(builder.toString());
		components.add(compenent.build());
		jsonarray.add(compenent.build().toString());
		if (jsonarray.size() > 1) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.add("extra", jsonarray.deepCopy());
			JSONObject json = new JSONObject();
			json.put("extra", components);
			json.put("text", "");
			//return json.toJSONString();
			return jsonObject.toString();
		}
		return compenent.build() + "";
	}

	/**
	 * Type your message/string text here. you use
	 * <p>
	 * §/& colorcode or <#55F758> for normal hex and
	 * <#5e4fa2:#f79459> for gradient (use §/&r to stop gradient).
	 *
	 * @param message your string message.
	 * @return spigot compatible translation.
	 */

	public static String toSpigotFormat(String message) {
		Matcher matcherGradient = GRADIENT_PATTERN.matcher(message);

		if (matcherGradient.find()) {
			message = createGradient(message);
			message = message.replace("§#", "<#");
		}
		Matcher matcher = HEX_PATTERN.matcher(message);
		while (matcher.find()) {
			String match = matcher.group(0);
			String[] hexSplitedToFitRBG = match.split("");
			if (!(hexSplitedToFitRBG.length >= 8))
				System.out.println("you has wrongly set up the hex color it shall be 6 in length");
			message = message.replace(match, "&x&" + hexSplitedToFitRBG[2] + "&" + hexSplitedToFitRBG[3] + "&" + hexSplitedToFitRBG[4] + "&" + hexSplitedToFitRBG[5] + "&" + hexSplitedToFitRBG[6] + "&" + hexSplitedToFitRBG[7]);
		}
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	/**
	 * Create an string with colors added on every letter (some are after the colorcode and to message length or to &r).
	 *
	 * @param message message you want to check and translate
	 * @return string with added gradient and rest uneffected some are outside the gradient range.
	 */

	private static String createGradient(String message) {

		Matcher gradiensMatcher = GRADIENT_PATTERN.matcher(message);
		StringBuilder specialSign = new StringBuilder();
		StringBuilder builder = new StringBuilder();
		boolean isFirstrun = true;
		int messageLength = message.length();

		while (gradiensMatcher.find()) {
			String match = gradiensMatcher.group(0);
			String[] split = match.split(":");
			int startGradient = message.indexOf(match);
			int stopGradient = checkForR(message.substring(startGradient)) != -1 ? startGradient + checkForR(message.substring(startGradient)) : message.length();

			String coloriseMsg = message.substring(startGradient, stopGradient).replace(match, "");
			for (String color : SPECIAL_SIGN) {
				if (coloriseMsg.contains(color)) {
					specialSign.append(color);
					coloriseMsg = coloriseMsg.replace(color, "");
				}
			}

			String[] messageFinal = coloriseMsg.split("");
			int step = messageFinal.length;
			if (split.length > 1) {
				String startColor = split[0].replace("<", "");
				Color firstColor = hexToRgb(startColor);
				Color endColor = hexToRgb(split[1].replace(">", ""));
				int stepRed = Math.abs(firstColor.getRed() - endColor.getRed()) / (step - 1);
				int stepGreen = Math.abs(firstColor.getGreen() - endColor.getGreen()) / (step - 1);
				int stepBlue = Math.abs(firstColor.getBlue() - endColor.getBlue()) / (step - 1);

				int[] direction = new int[]{
						firstColor.getRed() < endColor.getRed() ? +1 : -1,
						firstColor.getGreen() < endColor.getGreen() ? +1 : -1,
						firstColor.getBlue() < endColor.getBlue() ? +1 : -1};

				builder.append(message, 0, startGradient);
				for (int i = 0; i < step; i++) {
					String colors = convertRGBtoHex(firstColor.getRed() + ((stepRed * i) * direction[0]), firstColor.getGreen() + ((stepGreen * i) * direction[1]), firstColor.getBlue() + ((stepBlue * i) * direction[2]));
					if (isFirstrun) {
						builder.append("§").append(startColor).append(">").append(specialSign).append(messageFinal[i]);
						isFirstrun = false;
					} else {
						builder.append("§").append(colors).append(">").append(specialSign).append(messageFinal[i]);
					}
				}
				builder.append(message, stopGradient, messageLength);
			}
		}
		return builder.toString();
	}

	/**
	 * Convert rbg to hex.
	 *
	 * @param R red color.
	 * @param G green color.
	 * @param B blue color.
	 * @return hex color or 0 if rgb values is over 255 or below 0.
	 */
	private static String convertRGBtoHex(int R, int G, int B) {
		if ((R >= 0 && R <= 255)
				&& (G >= 0 && G <= 255)
				&& (B >= 0 && B <= 255)) {

			Color color = new Color(R, G, B);
			StringBuilder hex = new StringBuilder(Integer.toHexString(color.getRGB() & 0xffffff));
			while (hex.length() < 6) {
				hex.insert(0, "0");
			}
			hex.insert(0, "#");
			return hex.toString();
		}
		// The hex color code doesn't exist
		else
			return "0";
	}

	/**
	 * turn hex to rbg.
	 *
	 * @param colorStr hex you want to transform.
	 * @return RBGcolors.
	 */
	private static Color hexToRgb(String colorStr) {
		return new Color(
				Integer.valueOf(colorStr.substring(1, 3), 16),
				Integer.valueOf(colorStr.substring(3, 5), 16),
				Integer.valueOf(colorStr.substring(5, 7), 16));
	}

	/**
	 * Check if added &r  or §r.
	 *
	 * @param message the text you want to check.
	 * @return true if contains &r or §r.
	 */

	private static int checkForR(String message) {
		String msg = message.toLowerCase(Locale.ROOT);
		return msg.contains("&r") ? msg.indexOf("&r") : msg.contains("§r") ? msg.indexOf("§r") : -1;
	}

	/**
	 * Check if it valid color symbole
	 *
	 * @param message check color symbole
	 * @return true if it are valid color symbole.
	 */

	private static boolean checkIfColor(char message) {

		String[] colorcodes = ChatColors.ALL_CODES.split("");
		for (String color : colorcodes)
			if (color.equals(String.valueOf(message)))
				return true;
		return false;
	}

	/**
	 * Check if it valid hex or not.
	 *
	 * @param str you whant to check
	 * @return true if it vaild hex color.
	 */
	public static boolean isValidHexaCode(String str) {
		// Regex to check valid hexadecimal color code.
		String regex = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";

		// Compile the ReGex
		Pattern pattern = Pattern.compile(regex);

		// If the string is empty
		// return false
		if (str == null) {
			return false;
		}

		// Pattern class contains matcher() method
		// to find matching between given string
		// and regular expression.
		Matcher matcher = pattern.matcher(str);

		// Return if the string
		// matched the ReGex
		return matcher.matches();
	}
}
