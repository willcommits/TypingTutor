package typingTutor;
import java.util.concurrent.atomic.AtomicBoolean;
//Thread to monitor the word that has been typed.
public class CatchWord extends Thread {
	String target;
	static AtomicBoolean done ; //REMOVE
	static AtomicBoolean pause; //REMOVE
	private static  FallingWord[] words; //list of words
	private static  HungryWord[] hungryWords; //list of words
	private static int noWords; //how many
	private static Score score; //user score
	CatchWord(String typedWord) {
		target=typedWord;
	}

	public static synchronized void setWords(FallingWord[] wordList) {
		words=wordList;
		noWords = words.length;
	}

	public static synchronized void setWords(HungryWord[] hWords) {
		hungryWords=hWords;
	}
	public static synchronized void setScore(Score sharedScore) {
		score=sharedScore;
	}

	public static synchronized  void setFlags(AtomicBoolean d, AtomicBoolean p) {
		done=d;
		pause=p;
	}

	public void run() {
		int i=0;
		while (i<noWords) {
			while(pause.get()) {};
			int biggest=-1;
			int index=-1;
			if(!hungryWords[0].getWord().equals(null)){
				if(hungryWords[0].matchWord(target)){
					System.out.println( " score! '" + target); //for checking
					score.caughtWord(target.length());
					//FallingWord.increaseSpeed();
					break;
				}
			}
			if(words[i].getWord().equals(target)){
				for(int p=0;p<words.length;p++){
					if(words[p].getWord().equals(words[i].getWord())){
						if(biggest<words[p].getY()){
							biggest=words[p].getY();
							index=p;
						}

					}
				}
				if (words[index].matchWord(target)) {
					System.out.println( " score! '" + target); //for checking
					score.caughtWord(target.length());
					//FallingWord.increaseSpeed();
					break;
				}
			}




			i++;
		}

	}
}