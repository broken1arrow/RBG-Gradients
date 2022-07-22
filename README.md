## This liberary create mincraft compatible json strings and also have Spigot support.

[![](https://jitpack.io/v/broken1arrow/RBG-Gradients.svg)](https://jitpack.io/#broken1arrow/RBG-Gradients)
## It suport this color format.

* &5/§5 (normal color codes)
* <#55F758> (hex)
* <#5e4fa2:#f79459> (Gradiens format)
* gradiens_<#6B023E:...> your text here -> you need have at least 2 colors or more
* gradiens_hsv_<#6B023E:...> your text here -> you need have at least 2 colors or more
* gradiens_<#6B023E:...>_portion<0.2:...> your text here -> to set portion of every color (if you have two colors can you ony have 1 value in portion). 
* gradiens_hsv_<#6B023E:...>_portion<0.2:...> your text here -> to set portion of every color (if you have two colors can you ony have 1 value in portion).

## How to use colors.
You add colors as you normally do. Only diffrence are Gradiens.
You can do for example:
"&7this tex will not have gradient<#5e4fa2:#f79459>but this text will have gradient &6this will not have gradient."



## Two methods you can use.
```
//Json
TextTranslator.toComponent("your message");
//or use this So can you also set your default color.

TextTranslator.toComponent("your message","set defult color");

//spigot format.
TextTranslator.toSpigotFormat("your message");

//I have also added a check you can use if a string contains a valid hex color code.
//I already check it in the code, but if you need to check itself, you can use this.
TextTranslator.getInstance().isValidHexCode("your hex you want to check");

```
## To get the api
```

	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
	
     <dependencies>
 	<dependency>
	    <groupId>com.github.broken1arrow</groupId>
	    <artifactId>RBG-Gradients</artifactId>
	    <version>version</version>
	</dependency>
     </dependencies>
```
