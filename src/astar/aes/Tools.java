package astar.aes;

import java.util.Random;

public final class Tools
{
   /**
    * Commonly used so we precalc it.
    */
    private static  Random randomizer = new Random();

    public static final void setRandomizer(int seed) {
    	randomizer = new Random(seed);
    }
   /**
    * A method to obtain a random number between a miniumum and maximum
    * range.
    * @param min The minimum number of the range
    * @param max The maximum number of the range
    * @return
    */
   public static final int getRand(int min, int max)
   {
      int r = Math.abs(randomizer.nextInt());
      return (r % ((max - min + 1))) + min;
   }

   /**
   * Test for a collision between two rectangles using plane exclusion.
   * @return True if the rectangle for from the b coordinates intersects those
   * of a.
   */
   public static final boolean isIntersectingRect(int ax, int ay,
                        int aw, int ah,
                        int bx, int by,
                        int bw, int bh)
   {
      if (by + bh < ay || // is the bottom b above the top of a?
          by > ay + ah || // is the top of b below bottom of a?
          bx + bw < ax || // is the right of b to the left of a?
          bx > ax + aw)   // is the left of b to the right of a?
         return false;

      return true;
   }

   /**
    * Returns true if rectangle b is wholly within rectangle a
    */
   public static final boolean isRectWithinRect(int ax, int ay, int aw, int ah,
                                                int bx, int by, int bw, int bh)
   {
      if (isPointInRect(bx, by, ax, ay, aw, ah) &&
              isPointInRect(bx + bw, by, ax, ay, aw, ah) &&
              isPointInRect(bx, by + bh, ax, ay, aw, ah) &&
              isPointInRect(bx + bw, by + bh, ax, ay, aw, ah))
         return true;
      return false;
   }


   public static final boolean isPointInRect(int px, int py, int x, int y, int w, int h)
   {
      if (px < x) return false;
      if (px > x + w) return false;
      if (py < y) return false;
      if (py > y + h) return false;
      return true;
   }


   /**
    * Take an array of existing objects and expand it's size by a given number
    * of elements.
    * @param oldArray The array to expand.
    * @param expandBy The number of elements to expand the array by.
    * @return A new array (which is a copy of the original with space for more
    * elements.
    */
   public final static int[] expandArray(int[] oldArray, int expandBy)
   {
      int[] newArray = new int[oldArray.length + expandBy];
      System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
      return newArray;
   }


   private static final int MAX_STAR_WIDTH = 3;
   private static final int MAX_STAR_HEIGHT = 3;
}



















