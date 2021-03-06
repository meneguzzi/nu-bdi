import java.io.FileWriter;
import java.util.*;

/**
 * A (quick and dirty) program to generate BombWorld domains for testing with nu-BDI.
 * Parameters are:
 * 	<ul>
 * 		<li>the number of bombs to be defused in the domain; and</li>
 * 		<li>and the name of the file where the domain properties are to be stored.</li>
 * </ul>
 * @author meneguzzi
 *
 */
public class MakeBombWorld {

	private static final int TIME_STEP = 750;
	static final int MAXWALLPOSITION = 10;
	static final int MAXWALLLENGTH = 8;
	static final int MAXBOMBPOSITION = 10;
	static final int STARTWALLPOSITION = 4;
	static final int STARTBOMBPOSITION = 5;

	static Random r = new Random();

	public static void main(String args[]) throws Exception {

		for (int numberOfBombs = 1; numberOfBombs <= Integer.parseInt(args[0]); numberOfBombs++) {
			// numberOfBombs=Integer.parseInt(args[0]);
			Properties p = new Properties();
			p.setProperty("bins", "[bin(3,3,1)]");
			p.setProperty("unsafeChanges", "" + numberOfBombs);
			p.setProperty("agent", "p(4,4)");
			p.setProperty("unsafes", "[u(5,4)]");
			p.setProperty("unsafesNorm", "norm(prohibition, move(D), X==5 & Y==4, activateNorm0, deactivateNorm0, noUnsafe0)");

			HashSet<String> conf = new HashSet<String>();// records position of
															// unsafes
			for (int i = 1; i <= numberOfBombs; i++) {
				p.setProperty("unsafesChangeTime" + i, ""
						+ (TIME_STEP * (i + 1)));
				// generate a random unsafe
				boolean xUnsafe = r.nextBoolean();
				// we limit the length of the random to 10
				int unsafeLength = 1 + r.nextInt(MAXWALLLENGTH);
				// the x/y start position of the random will be
				// at a max of 10+numberOfBombs
				int startx = STARTWALLPOSITION
						+ r.nextInt(MAXWALLPOSITION + numberOfBombs);
				int starty = STARTWALLPOSITION
						+ r.nextInt(MAXWALLPOSITION + numberOfBombs);


				String sent = "[";

				if (xUnsafe) {

					int finishy = starty + unsafeLength;
					p.setProperty("unsafesChangeNorm" + i,
								"norm(prohibition, move(D), X==" + startx
										+ " & Y>" + starty + " & Y<" + finishy
										+ ", activateNorm" + i
										+ ", deactivateNorm" + i + ", noUnsafe"
										+ i + ")");
					for (int j = starty + 1; j < finishy -1 ; j ++ )
          {
						sent = sent + "u(" + startx + "," + j + "),";
						conf.add("" + startx + "," + j);
					}
					sent = sent + "u(" + startx + "," + (finishy - 1) + ")]";
					conf.add("" + startx + "," + (finishy - 1));
				} else {
					int finishx = startx + unsafeLength;
					p.setProperty("unsafesChangeNorm" + i,
								"norm(prohibition, move(D), Y==" + starty
										+ " & X>" + startx + " & X<" + finishx
										+ ", activateNorm" + i
										+ ", deactivateNorm" + i + ", noUnsafe"
										+ i + ")");
					for (int j = startx + 1; j < finishx -1 ; j ++)
          {
						sent = sent + "u(" + j + "," + starty + "),";
						conf.add("" + j + "," + starty);

					}
					sent = sent + "u(" + (finishx -1 )+ "," + starty + ")]";
					conf.add("" + (finishx-1)+ "," + starty);
				}
				p.setProperty("unsafesChangeNew" + i, sent);

			}

      Set<String> bombSet=new HashSet<String>();
			int bombsPlaced = 0;
			ArrayList<String> bombPos = new ArrayList<String>();
			while (bombsPlaced < numberOfBombs) {
				int x = STARTBOMBPOSITION
						+ r.nextInt(MAXBOMBPOSITION + numberOfBombs);
				int y = STARTBOMBPOSITION
						+ r.nextInt(MAXBOMBPOSITION + numberOfBombs);
				if (!conf.contains("" + x + "," + y) && !bombSet.contains(""+x+","+y)) 
        {
          bombSet.add(""+x+","+y);
					bombsPlaced++;
					bombPos.add("bomb(" + x + "," + y + ",1)");
				}
			}


			String bombString = "[";
			for (int i = 0; i < numberOfBombs - 1; i++)
				bombString = bombString + bombPos.get(i) + ",";
			bombString = bombString + bombPos.get(numberOfBombs - 1) + "]";
			p.setProperty("bombs", bombString);

			p.store(new FileWriter(args[1] + numberOfBombs+ ".properties"), "");
		}
	}
}
