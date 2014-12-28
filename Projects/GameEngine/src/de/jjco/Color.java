package de.jjco;

import org.lwjgl.opengl.GL11;

/**
 * This color class resembles the AWT {@link java.awt.Color} class.  The main difference
 * is that each component value lies in [0, 1].  Additionally, this class contains many
 * utility methods that allow ease of use, specifically with the GL API.
 * <p>
 * The default colors included in this class are the 140 web-safe colors.
 * These colors can be found at <a href="http://www.w3schools.com/html/html_colornames.asp">the W3S color page<a>.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public class Color implements Cloneable {
	private float rc = 0;
	private float gc = 0;
	private float bc = 0;
	private float ac = 1;
	
	/** Color object that represents "Alice Blue".<div style="background-color:#F0F8FF;border:1px solid black"><br><br><br></div> */
	public static final Color ALICE_BLUE = new Color(0xFFF8F0);
	/** Color object that represents "Antique White".<div style="background-color:#FAEBD7;border:1px solid black"><br><br><br></div> */
	public static final Color ANTIQUE_WHITE = new Color(0xD7EBFA);
	/** Color object that represents "Aqua".<div style="background-color:#00FFFF;border:1px solid black"><br><br><br></div> */
	public static final Color AQUA = new Color(0xFFFF00);
	/** Color object that represents "Aquamarine".<div style="background-color:#7FFFD4;border:1px solid black"><br><br><br></div> */
	public static final Color AQUAMARINE = new Color(0xD4FF7F);
	/** Color object that represents "Azure".<div style="background-color:#F0FFFF;border:1px solid black"><br><br><br></div> */
	public static final Color AZURE = new Color(0xFFFFF0);
	/** Color object that represents "Beige".<div style="background-color:#F5F5DC;border:1px solid black"><br><br><br></div> */
	public static final Color BEIGE = new Color(0xDCF5F5);
	/** Color object that represents "Bisque".<div style="background-color:#FFE4C4;border:1px solid black"><br><br><br></div> */
	public static final Color BISQUE = new Color(0xC4E4FF);
	/** Color object that represents "Black".<div style="background-color:#000000;border:1px solid black"><br><br><br></div> */
	public static final Color BLACK = new Color(0x000000);
	/** Color object that represents "Blanched Almond".<div style="background-color:#FFEBCD;border:1px solid black"><br><br><br></div> */
	public static final Color BLANCHED_ALMOND = new Color(0xCDEBFF);
	/** Color object that represents "Blue".<div style="background-color:#0000FF;border:1px solid black"><br><br><br></div> */
	public static final Color BLUE = new Color(0xFF0000);
	/** Color object that represents "Blue Violet".<div style="background-color:#8A2BE2;border:1px solid black"><br><br><br></div> */
	public static final Color BLUE_VIOLET = new Color(0xE22B8A);
	/** Color object that represents "Brown".<div style="background-color:#A52A2A;border:1px solid black"><br><br><br></div> */
	public static final Color BROWN = new Color(0x2A2AA5);
	/** Color object that represents "Burly Wood".<div style="background-color:#DEB887;border:1px solid black"><br><br><br></div> */
	public static final Color BURLY_WOOD = new Color(0x87B8DE);
	/** Color object that represents "Cadet Blue".<div style="background-color:#5F9EA0;border:1px solid black"><br><br><br></div> */
	public static final Color CADET_BLUE = new Color(0xA09E5F);
	/** Color object that represents "Chartreuse".<div style="background-color:#7FFF00;border:1px solid black"><br><br><br></div> */
	public static final Color CHARTREUSE = new Color(0x00FF7F);
	/** Color object that represents "Chocolate".<div style="background-color:#D2691E;border:1px solid black"><br><br><br></div> */
	public static final Color CHOCOLATE = new Color(0x1E69D2);
	/** Color object that represents "Coral".<div style="background-color:#FF7F50;border:1px solid black"><br><br><br></div> */
	public static final Color CORAL = new Color(0x507FFF);
	/** Color object that represents "Cornflower Blue".<div style="background-color:#6495ED;border:1px solid black"><br><br><br></div> */
	public static final Color CORNFLOWER_BLUE = new Color(0xED9564);
	/** Color object that represents "Cornsilk".<div style="background-color:#FFF8DC;border:1px solid black"><br><br><br></div> */
	public static final Color CORNSILK = new Color(0xDCF8FF);
	/** Color object that represents "Crimson".<div style="background-color:#DC143C;border:1px solid black"><br><br><br></div> */
	public static final Color CRIMSON = new Color(0x3C14DC);
	/** Color object that represents "Cyan".<div style="background-color:#00FFFF;border:1px solid black"><br><br><br></div> */
	public static final Color CYAN = new Color(0xFFFF00);
	/** Color object that represents "Dark Blue".<div style="background-color:#00008B;border:1px solid black"><br><br><br></div> */
	public static final Color DARK_BLUE = new Color(0x8B0000);
	/** Color object that represents "Dark Cyan".<div style="background-color:#008B8B;border:1px solid black"><br><br><br></div> */
	public static final Color DARK_CYAN = new Color(0x8B8B00);
	/** Color object that represents "Dark Golden Rod".<div style="background-color:#B8860B;border:1px solid black"><br><br><br></div> */
	public static final Color DARK_GOLDEN_ROD = new Color(0x0B86B8);
	/** Color object that represents "Dark Gray".<div style="background-color:#A9A9A9;border:1px solid black"><br><br><br></div> */
	public static final Color DARK_GRAY = new Color(0xA9A9A9);
	/** Color object that represents "Dark Green".<div style="background-color:#006400;border:1px solid black"><br><br><br></div> */
	public static final Color DARK_GREEN = new Color(0x006400);
	/** Color object that represents "Dark Khaki".<div style="background-color:#BDB76B;border:1px solid black"><br><br><br></div> */
	public static final Color DARK_KHAKI = new Color(0x6BB7BD);
	/** Color object that represents "Dark Magenta".<div style="background-color:#8B008B;border:1px solid black"><br><br><br></div> */
	public static final Color DARK_MAGENTA = new Color(0x8B008B);
	/** Color object that represents "Dark Olive Green".<div style="background-color:#556B2F;border:1px solid black"><br><br><br></div> */
	public static final Color DARK_OLIVE_GREEN = new Color(0x2F6B55);
	/** Color object that represents "Dark Orange".<div style="background-color:#FF8C00;border:1px solid black"><br><br><br></div> */
	public static final Color DARK_ORANGE = new Color(0x008CFF);
	/** Color object that represents "Dark Orchid".<div style="background-color:#9932CC;border:1px solid black"><br><br><br></div> */
	public static final Color DARK_ORCHID = new Color(0xCC3299);
	/** Color object that represents "Dark Red".<div style="background-color:#8B0000;border:1px solid black"><br><br><br></div> */
	public static final Color DARK_RED = new Color(0x00008B);
	/** Color object that represents "Dark Salmon".<div style="background-color:#E9967A;border:1px solid black"><br><br><br></div> */
	public static final Color DARK_SALMON = new Color(0x7A96E9);
	/** Color object that represents "Dark Sea Green".<div style="background-color:#8FBC8F;border:1px solid black"><br><br><br></div> */
	public static final Color DARK_SEA_GREEN = new Color(0x8FBC8F);
	/** Color object that represents "Dark Slate Blue".<div style="background-color:#483D8B;border:1px solid black"><br><br><br></div> */
	public static final Color DARK_SLATE_BLUE = new Color(0x8B3D48);
	/** Color object that represents "Dark Slate Gray".<div style="background-color:#2F4F4F;border:1px solid black"><br><br><br></div> */
	public static final Color DARK_SLATE_GRAY = new Color(0x4F4F2F);
	/** Color object that represents "Dark Turquoise".<div style="background-color:#00CED1;border:1px solid black"><br><br><br></div> */
	public static final Color DARK_TURQUOISE = new Color(0xD1CE00);
	/** Color object that represents "Dark Violet".<div style="background-color:#9400D3;border:1px solid black"><br><br><br></div> */
	public static final Color DARK_VIOLET = new Color(0xD30094);
	/** Color object that represents "Deep Pink".<div style="background-color:#FF1493;border:1px solid black"><br><br><br></div> */
	public static final Color DEEP_PINK = new Color(0x9314FF);
	/** Color object that represents "Deep Sky Blue".<div style="background-color:#00BFFF;border:1px solid black"><br><br><br></div> */
	public static final Color DEEP_SKY_BLUE = new Color(0xFFBF00);
	/** Color object that represents "Dim Gray".<div style="background-color:#696969;border:1px solid black"><br><br><br></div> */
	public static final Color DIM_GRAY = new Color(0x696969);
	/** Color object that represents "Dodger Blue".<div style="background-color:#1E90FF;border:1px solid black"><br><br><br></div> */
	public static final Color DODGER_BLUE = new Color(0xFF901E);
	/** Color object that represents "Fire Brick".<div style="background-color:#B22222;border:1px solid black"><br><br><br></div> */
	public static final Color FIRE_BRICK = new Color(0x2222B2);
	/** Color object that represents "Floral White".<div style="background-color:#FFFAF0;border:1px solid black"><br><br><br></div> */
	public static final Color FLORAL_WHITE = new Color(0xF0FAFF);
	/** Color object that represents "Forest Green".<div style="background-color:#228B22;border:1px solid black"><br><br><br></div> */
	public static final Color FOREST_GREEN = new Color(0x228B22);
	/** Color object that represents "Fuchsia".<div style="background-color:#FF00FF;border:1px solid black"><br><br><br></div> */
	public static final Color FUCHSIA = new Color(0xFF00FF);
	/** Color object that represents "Gainsboro".<div style="background-color:#DCDCDC;border:1px solid black"><br><br><br></div> */
	public static final Color GAINSBORO = new Color(0xDCDCDC);
	/** Color object that represents "Ghost White".<div style="background-color:#F8F8FF;border:1px solid black"><br><br><br></div> */
	public static final Color GHOST_WHITE = new Color(0xFFF8F8);
	/** Color object that represents "Gold".<div style="background-color:#FFD700;border:1px solid black"><br><br><br></div> */
	public static final Color GOLD = new Color(0x00D7FF);
	/** Color object that represents "Golden Rod".<div style="background-color:#DAA520;border:1px solid black"><br><br><br></div> */
	public static final Color GOLDEN_ROD = new Color(0x20A5DA);
	/** Color object that represents "Gray".<div style="background-color:#808080;border:1px solid black"><br><br><br></div> */
	public static final Color GRAY = new Color(0x808080);
	/** Color object that represents "Green".<div style="background-color:#008000;border:1px solid black"><br><br><br></div> */
	public static final Color GREEN = new Color(0x008000);
	/** Color object that represents "Green Yellow".<div style="background-color:#ADFF2F;border:1px solid black"><br><br><br></div> */
	public static final Color GREEN_YELLOW = new Color(0x2FFFAD);
	/** Color object that represents "Honey Dew".<div style="background-color:#F0FFF0;border:1px solid black"><br><br><br></div> */
	public static final Color HONEY_DEW = new Color(0xF0FFF0);
	/** Color object that represents "Hot Pink".<div style="background-color:#FF69B4;border:1px solid black"><br><br><br></div> */
	public static final Color HOT_PINK = new Color(0xB469FF);
	/** Color object that represents "Indian Red".<div style="background-color:#CD5C5C;border:1px solid black"><br><br><br></div> */
	public static final Color INDIAN_RED = new Color(0x5C5CCD);
	/** Color object that represents "Indigo".<div style="background-color:#4B0082;border:1px solid black"><br><br><br></div> */
	public static final Color INDIGO = new Color(0x82004B);
	/** Color object that represents "Ivory".<div style="background-color:#FFFFF0;border:1px solid black"><br><br><br></div> */
	public static final Color IVORY = new Color(0xF0FFFF);
	/** Color object that represents "Khaki".<div style="background-color:#F0E68C;border:1px solid black"><br><br><br></div> */
	public static final Color KHAKI = new Color(0x8CE6F0);
	/** Color object that represents "Lavender".<div style="background-color:#E6E6FA;border:1px solid black"><br><br><br></div> */
	public static final Color LAVENDER = new Color(0xFAE6E6);
	/** Color object that represents "Lavender Blush".<div style="background-color:#FFF0F5;border:1px solid black"><br><br><br></div> */
	public static final Color LAVENDER_BLUSH = new Color(0xF5F0FF);
	/** Color object that represents "Lawn Green".<div style="background-color:#7CFC00;border:1px solid black"><br><br><br></div> */
	public static final Color LAWN_GREEN = new Color(0x00FC7C);
	/** Color object that represents "Lemon Chiffon".<div style="background-color:#FFFACD;border:1px solid black"><br><br><br></div> */
	public static final Color LEMON_CHIFFON = new Color(0xCDFAFF);
	/** Color object that represents "Light Blue".<div style="background-color:#ADD8E6;border:1px solid black"><br><br><br></div> */
	public static final Color LIGHT_BLUE = new Color(0xE6D8AD);
	/** Color object that represents "Light Coral".<div style="background-color:#F08080;border:1px solid black"><br><br><br></div> */
	public static final Color LIGHT_CORAL = new Color(0x8080F0);
	/** Color object that represents "Light Cyan".<div style="background-color:#E0FFFF;border:1px solid black"><br><br><br></div> */
	public static final Color LIGHT_CYAN = new Color(0xFFFFE0);
	/** Color object that represents "Light Golden Rod Yellow".<div style="background-color:#FAFAD2;border:1px solid black"><br><br><br></div> */
	public static final Color LIGHT_GOLDEN_ROD_YELLOW = new Color(0xD2FAFA);
	/** Color object that represents "Light Gray".<div style="background-color:#D3D3D3;border:1px solid black"><br><br><br></div> */
	public static final Color LIGHT_GRAY = new Color(0xD3D3D3);
	/** Color object that represents "Light Green".<div style="background-color:#90EE90;border:1px solid black"><br><br><br></div> */
	public static final Color LIGHT_GREEN = new Color(0x90EE90);
	/** Color object that represents "Light Pink".<div style="background-color:#FFB6C1;border:1px solid black"><br><br><br></div> */
	public static final Color LIGHT_PINK = new Color(0xC1B6FF);
	/** Color object that represents "Light Salmon".<div style="background-color:#FFA07A;border:1px solid black"><br><br><br></div> */
	public static final Color LIGHT_SALMON = new Color(0x7AA0FF);
	/** Color object that represents "Light Sea Green".<div style="background-color:#20B2AA;border:1px solid black"><br><br><br></div> */
	public static final Color LIGHT_SEA_GREEN = new Color(0xAAB220);
	/** Color object that represents "Light Sky Blue".<div style="background-color:#87CEFA;border:1px solid black"><br><br><br></div> */
	public static final Color LIGHT_SKY_BLUE = new Color(0xFACE87);
	/** Color object that represents "Light Slate Gray".<div style="background-color:#778899;border:1px solid black"><br><br><br></div> */
	public static final Color LIGHT_SLATE_GRAY = new Color(0x998877);
	/** Color object that represents "Light Steel Blue".<div style="background-color:#B0C4DE;border:1px solid black"><br><br><br></div> */
	public static final Color LIGHT_STEEL_BLUE = new Color(0xDEC4B0);
	/** Color object that represents "Light Yellow".<div style="background-color:#FFFFE0;border:1px solid black"><br><br><br></div> */
	public static final Color LIGHT_YELLOW = new Color(0xE0FFFF);
	/** Color object that represents "Lime".<div style="background-color:#00FF00;border:1px solid black"><br><br><br></div> */
	public static final Color LIME = new Color(0x00FF00);
	/** Color object that represents "Lime Green".<div style="background-color:#32CD32;border:1px solid black"><br><br><br></div> */
	public static final Color LIME_GREEN = new Color(0x32CD32);
	/** Color object that represents "Linen".<div style="background-color:#FAF0E6;border:1px solid black"><br><br><br></div> */
	public static final Color LINEN = new Color(0xE6F0FA);
	/** Color object that represents "Magenta".<div style="background-color:#FF00FF;border:1px solid black"><br><br><br></div> */
	public static final Color MAGENTA = new Color(0xFF00FF);
	/** Color object that represents "Maroon".<div style="background-color:#800000;border:1px solid black"><br><br><br></div> */
	public static final Color MAROON = new Color(0x000080);
	/** Color object that represents "Medium Aqua Marine".<div style="background-color:#66CDAA;border:1px solid black"><br><br><br></div> */
	public static final Color MEDIUM_AQUA_MARINE = new Color(0xAACD66);
	/** Color object that represents "Medium Blue".<div style="background-color:#0000CD;border:1px solid black"><br><br><br></div> */
	public static final Color MEDIUM_BLUE = new Color(0xCD0000);
	/** Color object that represents "Medium Orchid".<div style="background-color:#BA55D3;border:1px solid black"><br><br><br></div> */
	public static final Color MEDIUM_ORCHID = new Color(0xD355BA);
	/** Color object that represents "Medium Purple".<div style="background-color:#9370DB;border:1px solid black"><br><br><br></div> */
	public static final Color MEDIUM_PURPLE = new Color(0xDB7093);
	/** Color object that represents "Medium Sea Green".<div style="background-color:#3CB371;border:1px solid black"><br><br><br></div> */
	public static final Color MEDIUM_SEA_GREEN = new Color(0x71B33C);
	/** Color object that represents "Medium Slate Blue".<div style="background-color:#7B68EE;border:1px solid black"><br><br><br></div> */
	public static final Color MEDIUM_SLATE_BLUE = new Color(0xEE687B);
	/** Color object that represents "Medium Spring Green".<div style="background-color:#00FA9A;border:1px solid black"><br><br><br></div> */
	public static final Color MEDIUM_SPRING_GREEN = new Color(0x9AFA00);
	/** Color object that represents "Medium Turquoise".<div style="background-color:#48D1CC;border:1px solid black"><br><br><br></div> */
	public static final Color MEDIUM_TURQUOISE = new Color(0xCCD148);
	/** Color object that represents "Medium Violet Red".<div style="background-color:#C71585;border:1px solid black"><br><br><br></div> */
	public static final Color MEDIUM_VIOLET_RED = new Color(0x8515C7);
	/** Color object that represents "Midnight Blue".<div style="background-color:#191970;border:1px solid black"><br><br><br></div> */
	public static final Color MIDNIGHT_BLUE = new Color(0x701919);
	/** Color object that represents "Mint Cream".<div style="background-color:#F5FFFA;border:1px solid black"><br><br><br></div> */
	public static final Color MINT_CREAM = new Color(0xFAFFF5);
	/** Color object that represents "Misty Rose".<div style="background-color:#FFE4E1;border:1px solid black"><br><br><br></div> */
	public static final Color MISTY_ROSE = new Color(0xE1E4FF);
	/** Color object that represents "Moccasin".<div style="background-color:#FFE4B5;border:1px solid black"><br><br><br></div> */
	public static final Color MOCCASIN = new Color(0xB5E4FF);
	/** Color object that represents "Navajo White".<div style="background-color:#FFDEAD;border:1px solid black"><br><br><br></div> */
	public static final Color NAVAJO_WHITE = new Color(0xADDEFF);
	/** Color object that represents "Navy".<div style="background-color:#000080;border:1px solid black"><br><br><br></div> */
	public static final Color NAVY = new Color(0x800000);
	/** Color object that represents "Old Lace".<div style="background-color:#FDF5E6;border:1px solid black"><br><br><br></div> */
	public static final Color OLD_LACE = new Color(0xE6F5FD);
	/** Color object that represents "Olive".<div style="background-color:#808000;border:1px solid black"><br><br><br></div> */
	public static final Color OLIVE = new Color(0x008080);
	/** Color object that represents "Olive Drab".<div style="background-color:#6B8E23;border:1px solid black"><br><br><br></div> */
	public static final Color OLIVE_DRAB = new Color(0x238E6B);
	/** Color object that represents "Orange".<div style="background-color:#FFA500;border:1px solid black"><br><br><br></div> */
	public static final Color ORANGE = new Color(0x00A5FF);
	/** Color object that represents "Orange Red".<div style="background-color:#FF4500;border:1px solid black"><br><br><br></div> */
	public static final Color ORANGE_RED = new Color(0x0045FF);
	/** Color object that represents "Orchid".<div style="background-color:#DA70D6;border:1px solid black"><br><br><br></div> */
	public static final Color ORCHID = new Color(0xD670DA);
	/** Color object that represents "Pale Golden Rod".<div style="background-color:#EEE8AA;border:1px solid black"><br><br><br></div> */
	public static final Color PALE_GOLDEN_ROD = new Color(0xAAE8EE);
	/** Color object that represents "Pale Green".<div style="background-color:#98FB98;border:1px solid black"><br><br><br></div> */
	public static final Color PALE_GREEN = new Color(0x98FB98);
	/** Color object that represents "Pale Turquoise".<div style="background-color:#AFEEEE;border:1px solid black"><br><br><br></div> */
	public static final Color PALE_TURQUOISE = new Color(0xEEEEAF);
	/** Color object that represents "Pale Violet Red".<div style="background-color:#DB7093;border:1px solid black"><br><br><br></div> */
	public static final Color PALE_VIOLET_RED = new Color(0x9370DB);
	/** Color object that represents "Papaya Whip".<div style="background-color:#FFEFD5;border:1px solid black"><br><br><br></div> */
	public static final Color PAPAYA_WHIP = new Color(0xD5EFFF);
	/** Color object that represents "Peach Puff".<div style="background-color:#FFDAB9;border:1px solid black"><br><br><br></div> */
	public static final Color PEACH_PUFF = new Color(0xB9DAFF);
	/** Color object that represents "Peru".<div style="background-color:#CD853F;border:1px solid black"><br><br><br></div> */
	public static final Color PERU = new Color(0x3F85CD);
	/** Color object that represents "Pink".<div style="background-color:#FFC0CB;border:1px solid black"><br><br><br></div> */
	public static final Color PINK = new Color(0xCBC0FF);
	/** Color object that represents "Plum".<div style="background-color:#DDA0DD;border:1px solid black"><br><br><br></div> */
	public static final Color PLUM = new Color(0xDDA0DD);
	/** Color object that represents "Powder Blue".<div style="background-color:#B0E0E6;border:1px solid black"><br><br><br></div> */
	public static final Color POWDER_BLUE = new Color(0xE6E0B0);
	/** Color object that represents "Purple".<div style="background-color:#800080;border:1px solid black"><br><br><br></div> */
	public static final Color PURPLE = new Color(0x800080);
	/** Color object that represents "Red".<div style="background-color:#FF0000;border:1px solid black"><br><br><br></div> */
	public static final Color RED = new Color(0x0000FF);
	/** Color object that represents "Rosy Brown".<div style="background-color:#BC8F8F;border:1px solid black"><br><br><br></div> */
	public static final Color ROSY_BROWN = new Color(0x8F8FBC);
	/** Color object that represents "Royal Blue".<div style="background-color:#4169E1;border:1px solid black"><br><br><br></div> */
	public static final Color ROYAL_BLUE = new Color(0xE16941);
	/** Color object that represents "Saddle Brown".<div style="background-color:#8B4513;border:1px solid black"><br><br><br></div> */
	public static final Color SADDLE_BROWN = new Color(0x13458B);
	/** Color object that represents "Salmon".<div style="background-color:#FA8072;border:1px solid black"><br><br><br></div> */
	public static final Color SALMON = new Color(0x7280FA);
	/** Color object that represents "Sandy Brown".<div style="background-color:#F4A460;border:1px solid black"><br><br><br></div> */
	public static final Color SANDY_BROWN = new Color(0x60A4F4);
	/** Color object that represents "Sea Green".<div style="background-color:#2E8B57;border:1px solid black"><br><br><br></div> */
	public static final Color SEA_GREEN = new Color(0x578B2E);
	/** Color object that represents "Sea Shell".<div style="background-color:#FFF5EE;border:1px solid black"><br><br><br></div> */
	public static final Color SEA_SHELL = new Color(0xEEF5FF);
	/** Color object that represents "Sienna".<div style="background-color:#A0522D;border:1px solid black"><br><br><br></div> */
	public static final Color SIENNA = new Color(0x2D52A0);
	/** Color object that represents "Silver".<div style="background-color:#C0C0C0;border:1px solid black"><br><br><br></div> */
	public static final Color SILVER = new Color(0xC0C0C0);
	/** Color object that represents "Sky Blue".<div style="background-color:#87CEEB;border:1px solid black"><br><br><br></div> */
	public static final Color SKY_BLUE = new Color(0xEBCE87);
	/** Color object that represents "Slate Blue".<div style="background-color:#6A5ACD;border:1px solid black"><br><br><br></div> */
	public static final Color SLATE_BLUE = new Color(0xCD5A6A);
	/** Color object that represents "Slate Gray".<div style="background-color:#708090;border:1px solid black"><br><br><br></div> */
	public static final Color SLATE_GRAY = new Color(0x908070);
	/** Color object that represents "Snow".<div style="background-color:#FFFAFA;border:1px solid black"><br><br><br></div> */
	public static final Color SNOW = new Color(0xFAFAFF);
	/** Color object that represents "Spring Green".<div style="background-color:#00FF7F;border:1px solid black"><br><br><br></div> */
	public static final Color SPRING_GREEN = new Color(0x7FFF00);
	/** Color object that represents "Steel Blue".<div style="background-color:#4682B4;border:1px solid black"><br><br><br></div> */
	public static final Color STEEL_BLUE = new Color(0xB48246);
	/** Color object that represents "Tan".<div style="background-color:#D2B48C;border:1px solid black"><br><br><br></div> */
	public static final Color TAN = new Color(0x8CB4D2);
	/** Color object that represents "Teal".<div style="background-color:#008080;border:1px solid black"><br><br><br></div> */
	public static final Color TEAL = new Color(0x808000);
	/** Color object that represents "Thistle".<div style="background-color:#D8BFD8;border:1px solid black"><br><br><br></div> */
	public static final Color THISTLE = new Color(0xD8BFD8);
	/** Color object that represents "Tomato".<div style="background-color:#FF6347;border:1px solid black"><br><br><br></div> */
	public static final Color TOMATO = new Color(0x4763FF);
	/** Color object that represents "Turquoise".<div style="background-color:#40E0D0;border:1px solid black"><br><br><br></div> */
	public static final Color TURQUOISE = new Color(0xD0E040);
	/** Color object that represents "Violet".<div style="background-color:#EE82EE;border:1px solid black"><br><br><br></div> */
	public static final Color VIOLET = new Color(0xEE82EE);
	/** Color object that represents "Wheat".<div style="background-color:#F5DEB3;border:1px solid black"><br><br><br></div> */
	public static final Color WHEAT = new Color(0xB3DEF5);
	/** Color object that represents "White".<div style="background-color:#FFFFFF;border:1px solid black"><br><br><br></div> */
	public static final Color WHITE = new Color(0xFFFFFF);
	/** Color object that represents "White Smoke".<div style="background-color:#F5F5F5;border:1px solid black"><br><br><br></div> */
	public static final Color WHITE_SMOKE = new Color(0xF5F5F5);
	/** Color object that represents "Yellow".<div style="background-color:#FFFF00;border:1px solid black"><br><br><br></div> */
	public static final Color YELLOW = new Color(0x00FFFF);
	/** Color object that represents "Yellow Green".<div style="background-color:#9ACD32;border:1px solid black"><br><br><br></div> */
	public static final Color YELLOW_GREEN = new Color(0x32CD9A);

	
	/**
	 * Creates a new color.
	 * 
	 * @param rgba the RGB or RGBA value
	 */
	public Color(int rgba) {
		setRGBA(rgba);
		
		if (ac == 0) {
			ac = 1;
		}
	}
	
	/**
	 * Creates a new color.
	 * 
	 * @param r the red component, from 0-255
	 * @param g the green component, from 0-255
	 * @param b the blue component, from 0-255
	 */
	public Color(int r, int g, int b) {
		rc = clamp((float)r / 255f);
		gc = clamp((float)g / 255f);
		bc = clamp((float)b / 255f);
	}
	
	/**
	 * Creates a new color.
	 * 
	 * @param r the red component, from 0-255
	 * @param g the green component, from 0-255
	 * @param b the blue component, from 0-255
	 * @param a the alpha component, from 0-255
	 */
	public Color(int r, int g, int b, int a) {
		rc = clamp((float)r / 255f);
		gc = clamp((float)g / 255f);
		bc = clamp((float)b / 255f);
		ac = clamp((float)a / 255f);
	}
	
	/**
	 * Creates a new color.
	 * 
	 * @param r the red component, from 0-1
	 * @param g the green component, from 0-1
	 * @param b the blue component, from 0-1
	 */
	public Color(float r, float g, float b) {
		rc = clamp(r);
		gc = clamp(g);
		bc = clamp(b);
	}
	
	/**
	 * Creates a new color.
	 * 
	 * @param r the red component, from 0-1
	 * @param g the green component, from 0-1
	 * @param b the blue component, from 0-1
	 * @param a the alpha component, from 0-1
	 */
	public Color(float r, float g, float b, float a) {
		rc = clamp(r);
		gc = clamp(g);
		bc = clamp(b);
		ac = clamp(a);
	}
	
	/**
	 * Creates a new color based off of the specified AWT Color.
	 * 
	 * @param c the color to replicate
	 */
	public Color(java.awt.Color c) {
		rc = c.getRed() / 255f;
		gc = c.getGreen() / 255f;
		bc = c.getBlue() / 255f;
		ac = c.getAlpha() / 255f;
	}
	
	/**
	 * @return the red component, from 0-1
	 */
	public float getRed() {
		return (rc);
	}
	
	/**
	 * @return the red component, from 0-255
	 */
	public int getRedInt() {
		return (int) (rc * 255f);
	}
	
	/**
	 * @return the green component, from 0-1
	 */
	public float getGreen() {
		return (gc);
	}
	
	/**
	 * @return the green component, from 0-255
	 */
	public int getGreenInt() {
		return (int) (gc * 255);
	}
	
	/**
	 * @return the blue component, from 0-1
	 */
	public float getBlue() {
		return (bc);
	}
	
	/**
	 * @return the blue component, from 0-255
	 */
	public int getBlueInt() {
		return (int) (bc * 255f);
	}
	
	/**
	 * @return the alpha component, from 0-1
	 */
	public float getAlpha() {
		return (ac);
	}
	
	/**
	 * @return the red component, from 0-255
	 */
	public int getAlphaInt() {
		return (int) (ac * 255f);
	}
	
	/**
	 * @return the RGB int
	 */
	public int getRGBA() {
		return ((getRedInt() << 24) | (getGreenInt() << 16) | (getBlueInt() << 8) | getAlphaInt());
	}
	
	/**
	 * @param r the red component, from 0-255
	 */
	public void setRed(int r) {
		rc = clamp((float)r / 255f);
	}
	
	/**
	 * @param r the red component, from 0-1
	 */
	public void setRed(float r) {
		rc = clamp(r);
	}
	
	/**
	 * @param g the green component, from 0-255
	 */
	public void setGreen(int g) {
		gc = clamp((float)g / 255f);
	}
	
	/**
	 * @param g the green component, from 0-1
	 */
	public void setGreen(float g) {
		gc = clamp(g);
	}
	
	/**
	 * @param b the blue component, from 0-255
	 */
	public void setBlue(int b) {
		bc = clamp((float)b / 255f);
	}
	
	/**
	 * @param b the blue component, from 0-1
	 */
	public void setBlue(float b) {
		bc = clamp(b);
	}
	
	/**
	 * @param a the alpha component, from 0-255
	 */
	public void setAlpha(int a) {
		ac = clamp((float)a / 255f);
	}
	
	/**
	 * @param a the alpha component, from 0-1
	 */
	public void setAlpha(float a) {
		ac = clamp(a);
	}
	
	/**
	 * @param rgb the RGBA int
	 */
	public void setRGBA(int rgb) {
		setAlpha((rgb >> 24) & 0xff);
		setBlue((rgb >> 16) & 0xff);
		setGreen((rgb >> 8) & 0xff);
		setRed(rgb & 0xff);
	}
	
	/**
	 * @param rgb the RGB int
	 */
	public void setRGB(int rgb) {
		setAlpha(0xff);
		setBlue((rgb >> 16) & 0xff);
		setGreen((rgb >> 8) & 0xff);
		setRed(rgb & 0xff);
	}
	
	/**
	 * @param c the color to copy components from
	 */
	public void setColor(Color c) {
		rc = c.getRed();
		gc = c.getGreen();
		bc = c.getBlue();
		ac = c.getAlpha();
	}
	
	/**
	 * Binds this color to the OpenGL renderer.
	 */
	public void bind() {
		GL11.glColor4f(rc, gc, bc, ac);
	}
	
	/**
	 * Scales the components by a uniform amount
	 * 
	 * @param f the multiplier
	 */
	public void scale(float f) {
		scale(f, f, f, f);
	}
	
	/**
	 * Scales each component by an amount
	 * 
	 * @param r the red multiplier
	 * @param g the green multiplier
	 * @param b the blue multiplier
	 * @param a the alpha multiplier
	 */
	public void scale(float r, float g, float b, float a) {
		rc = clamp(rc * r);
		gc = clamp(gc * g);
		bc = clamp(bc * b);
		ac = clamp(ac * a);
	}
	
	/**
	 * Adds a uniform amount to each component
	 * 
	 * @param f the amount
	 */
	public void add(float f) {
		add(f, f, f, f);
	}
	
	/**
	 * Adds to each component
	 * 
	 * @param r the red amount
	 * @param g the green amount
	 * @param b the blue amount
	 * @param a the alpha amount
	 */
	public void add(float r, float g, float b, float a) {
		rc = clamp(rc + r);
		gc = clamp(gc + g);
		bc = clamp(bc + b);
		ac = clamp(ac + a);
	}
	
	@Override
	public boolean equals(Object o) {
		if ( o instanceof Color ) {
			Color c = (Color) o;
			return (c.getRed() == rc && c.getGreen() == gc && c.getBlue() == bc && c.getAlpha() == ac);
		}
		
		return (false);
	}
	
	@Override
	public int hashCode() {
		return (getRGBA());
	}
	
	@Override
	public Color clone() {
		return (new Color(rc, gc, bc, ac));
	}
	
	private float clamp(float f) {
		// Preferred in place of Math.min(1, Math.max(0, f))?
		
		if (f > 1) {
			f = 1;
		} else if (f < 0) {
			f = 0;
		}
		
		return (f);
	}
}
