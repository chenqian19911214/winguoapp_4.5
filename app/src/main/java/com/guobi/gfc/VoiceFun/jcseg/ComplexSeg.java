package com.guobi.gfc.VoiceFun.jcseg;

import java.io.Reader;
import java.util.ArrayList;
//import java.util.Iterator;

import com.guobi.gfc.VoiceFun.jcseg.core.IChunk;
import com.guobi.gfc.VoiceFun.jcseg.core.IDictionary;
import com.guobi.gfc.VoiceFun.jcseg.core.ILexicon;
import com.guobi.gfc.VoiceFun.jcseg.core.IWord;

/**
 * Complex segment for JCSeg, has implements the ASegment class. <br />
 * this will need the filter work of four filter rule: <br />
 * <ul>
 * <li>1.maximum match chunk.</li>
 * <li>2.largest average word length.</li>
 * <li>3.smallest variance of words length.</li>
 * <li>4.largest sum of degree of morphemic freedom of one-character words.</li>
 * </ul>
 *
 * @author chenxin <br />
 * @email chenxin619315@gmail.com <br />
 * {@link http://www.webssky.com}
 */
public class ComplexSeg extends ASegment {

    public ComplexSeg() throws Exception {
        super(false);
    }

    public ComplexSeg(boolean train) throws Exception {
        super(train);
    }

    public ComplexSeg(Reader input, boolean train) throws Exception {
        super(input, train);
    }

    public ComplexSeg(Reader input, IDictionary dic, boolean train) throws Exception {
        super(input, dic, train);
    }

    /**
     * @see ASegment#getBestCJKChunk(char[], int)
     */
    @Override
    public IChunk getBestCJKChunk(char chars[], int index) {

        IWord[] mwords = getNextMatch(chars, index), mword2, mword3;
        //System.out.println("test w: " + Arrays.toString(mwords));
        /*if ( mwords.length == 1
				&& mwords[0].getType() == ILexicon.UNMATCH_CJK_WORD ) {
			
			System.out.println("test: 0: " + (new Chunk(new IWord[]{mwords[0]})));
			
			return new Chunk(new IWord[]{mwords[0]});
		}*/

        int idx_2, idx_3;
        ArrayList<IChunk> chunkArr = new ArrayList<IChunk>();

        for (int x = 0; x < mwords.length; x++) {
            //the second layer
            idx_2 = index + mwords[x].getLength();
            if (idx_2 < chars.length) {
                mword2 = getNextMatch(chars, idx_2);
				/*
				 * the first try for the second layer
				 * returned a UNMATCH_CJK_WORD
				 * here, just return the largest length word in
				 * the first layer. 
				 */
				/*if ( mword2.length == 1
						&& mword2[0].getType() == ILexicon.UNMATCH_CJK_WORD ) {
					
					System.out.println("test: 1: " + (new Chunk(new IWord[]{mwords[mwords.length - 1]})));
					return new Chunk(new IWord[]{mwords[mwords.length - 1]});
				} */
                for (int y = 0; y < mword2.length; y++) {
                    //the third layer
                    idx_3 = idx_2 + mword2[y].getLength();
                    if (idx_3 < chars.length) {
                        mword3 = getNextMatch(chars, idx_3);
                        for (int z = 0; z < mword3.length; z++) {
                            ArrayList<IWord> wArr = new ArrayList<IWord>(3);
                            wArr.add(mwords[x]);
                            wArr.add(mword2[y]);
                            if (mword3[z].getType() != ILexicon.UNMATCH_CJK_WORD)
                                wArr.add(mword3[z]);

                            IWord[] words = new IWord[wArr.size()];
                            wArr.toArray(words);
                            wArr = null;

                            chunkArr.add(new Chunk(words));
                        }
                    } else {
                        chunkArr.add(new Chunk(new IWord[]{mwords[x], mword2[y]}));
                    }
                }
            } else {
                chunkArr.add(new Chunk(new IWord[]{mwords[x]}));
            }
        }

        if (chunkArr.size() == 1) {
            //System.out.println("test: 2: " + (chunkArr.get(0)));
            return chunkArr.get(0);
        }
		
/*		Iterator<IChunk> it = chunkArr.iterator();
		while ( it.hasNext() ) {
			System.out.println(it.next());
		}
		System.out.println("-+---------------------+-");*/

        IChunk[] chunks = new IChunk[chunkArr.size()];
        chunkArr.toArray(chunks);
        //chunkArr.clear();

        chunkArr = null;
        mwords = null;
        mword2 = null;
        mword3 = null;

        return filterChunks(chunks);
    }

    /**
     * filter the chunks with the four rule.
     *
     * @param chunks
     * @return IWord
     */
    private IChunk filterChunks(IChunk[] chunks) {
        //call the maximum match rule.
        IChunk[] afterChunks = MMRule.createRule().call(chunks);
        if (afterChunks.length >= 2) {
            //call the largest average rule.
            afterChunks = LAWLRule.createRule().call(afterChunks);
            if (afterChunks.length >= 2) {
                //call the smallest variance rule.
                afterChunks = SVWLRule.createRule().call(afterChunks);
                if (afterChunks.length >= 2) {
                    //call the largest sum of degree of morphemic freedom rule.
                    afterChunks = LSWMFRule.createRule().call(afterChunks);
                    if (afterChunks.length >= 2) {

                        //return LASTRule.createRule().call(afterChunks).getWords()[0];
                        afterChunks = new IChunk[]{LASTRule.createRule().call(afterChunks)};
                    }
                }
            }
        }
        return afterChunks[0];
    }
	
	/*private IChunk filterChunks(IChunk[] chunks) {
		//call the maximum match rule.
		System.out.println("test: " + Arrays.toString(chunks));
		IChunk[] afterChunks = MMRule.createRule().call(chunks);
		if ( afterChunks.length >= 2 ) {
			//call the largest average rule.
			//afterChunks = LAWLRule.createRule().call(afterChunks);
			//if ( afterChunks.length >= 2 ) {
				//call the smallest variance rule.
		
				afterChunks = new IChunk[]{LASTRule.createRule().call(afterChunks)};
			//} 
		}
		
		System.out.println("test: select chunks: " + afterChunks[0]);
		return afterChunks[0];
	}*/


}
