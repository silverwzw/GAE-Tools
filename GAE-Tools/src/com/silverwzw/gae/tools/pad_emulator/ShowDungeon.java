package com.silverwzw.gae.tools.pad_emulator;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.silverwzw.servlet.ActionHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


final public class ShowDungeon implements ActionHandler {
	public void serv(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String dungeon,enm,itm,playerID;
		PrintWriter o;
		int i, count;
		
		o = resp.getWriter();
		
		if (req.getParameter("pid") == null) {
			playerID = "324363124";
		} else {
			playerID = req.getParameter("pid");
		}
		
		dungeon = (String)(PadEmulatorSettings.instance(playerID).dungeon.getDungeonString());
		if (dungeon == null) {
			resp.getWriter().print("No Dungeon Data Found");
			return;
		}
		
		Pattern pseq = Pattern.compile("\\{\\s*\"seq\"\\s*:\\s*\"\\d+\".+?\\[.+?}\\s*\\]\\s*\\}");
		Pattern penm = Pattern.compile("\"num\"\\s*?:\"(\\d+?)\"");
		Pattern pitm = Pattern.compile("\"item\"\\s*?:\"(\\d+?)\"");
		
		Matcher mseq = pseq.matcher(dungeon);
		i = 1;
		count = 0;
		
		o.println("<html><head><title>Silverwzw's Puzzle & Dragon Cracker - Dungeon lookup</title><script src=\"/pad/monsterDB.js\"><script src='/pad/monster.js'></script></head><body>");
		
		enm = "";
		itm = "";
		
		while (mseq.find()) {
			String seq = mseq.group(0);
			
			enm += "<br /></br>Wave " + ((Integer)i).toString() + ":<br />";
			
			Matcher menm = penm.matcher(seq);
			
			while (menm.find()) {
				enm += "<script>document.write(show(" + menm.group(1) + "));</script>";
			}
			
			Matcher mitm = pitm.matcher(seq);
			while (mitm.find()) {
				String bon = mitm.group(1);
				if (!bon.equals("0")) {
					if (Integer.parseInt(bon) == 9900) {
						count ++;
					} else {
						itm += "<script>document.write(show(" + bon + "));</script>";
					}
				}
			}
			i++;
		}
		
		o.println("Bonus :<br /> Box * " + count + "<br />");
		o.println(itm);
		o.println(enm);
		o.println("</body></html>");
	}
}
