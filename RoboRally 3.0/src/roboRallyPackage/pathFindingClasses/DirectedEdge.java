/**
 * 
 */
package roboRallyPackage.pathFindingClasses;

import be.kuleuven.cs.som.annotate.*;

/**
 *  The <tt>DirectedEdge</tt> class represents a weighted edge in an directed graph.
 *  <p>
 *  For additional documentation, see <a href="http://algs4.cs.princeton.edu/44sp">Section 4.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *  
 * @version 26 april 2012
 * @author Jonas Schouterden (r0260385) & Nele Rober (r0262954)
 * 			 Bachelor Ingenieurswetenschappen, KULeuven
 */

public class DirectedEdge
{ 
    private final int v;
    private final int w;
    private final double weight;

   /**
     * Create a directed edge from v to w with given weight.
     */
    public DirectedEdge(int v, int w, double weight) {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

   /**
     * Return the vertex where this edge begins.
     */
    @Basic
    public int from() {
        return v;
    }

   /**
     * Return the vertex where this edge ends.
     */
    @Basic
    public int to() {
        return w;
    }

   /**
     * Return the weight of this edge.
     */
    @Basic
    public double weight() { return weight; }

   /**
     * Return a string representation of this edge.
     */
    @Override
    public String toString() {
        return v + "->" + w + " " + String.format("%5.2f", weight);
    }
}