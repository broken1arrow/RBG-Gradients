## This liberary create mincraft compatible json strings and also have Spigot support.

## It suport this color format.

* &5/§5 (normal color codes)
* <#55F758> (hex)
* <#5e4fa2:#f79459> (Gradiens format)

## How to use colors.
You add colors as you normal do. Only diffrence are Gradiens.
You can do for example:
"this tex will not have gradient<#5e4fa2:#f79459>but this text will have gradient &rthis will not have gradient."
You can add colorcode after &r it will work use§r too or use big litter like this &R.


## Two methods you can use.
```
//Json
TextTranslator.toCompenent("your message");
//or use this So can you also set own defult color.

TextTranslator.toCompenent("your message","set defult color");

//spigot format.
TextTranslator.toSpigotFormat("your message");

//I have also added check you can use if a string contains valid hex colorcode.
//I alredy check it in the code, but if you need chexk it self, you can use this.
TextTranslator.isValidHexaCode("your hex you want to check");

```
## To get the api
```

	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
	
   </repositories>
 	<dependency>
	    <groupId>com.github.broken1arrow</groupId>
	    <artifactId>RBG-Gradients</artifactId>
	    <version>version</version>
	</dependency>
     </dependencies>
```
