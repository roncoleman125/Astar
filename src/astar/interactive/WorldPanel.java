/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package astar.interactive;

import astar.aes.World;
import astar.util.Node;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;
import javax.swing.JPanel;

/**
 *
 * @author roncoleman
 */
public class WorldPanel extends JPanel implements MouseListener, MouseMotionListener {
    public final int CELL_SIZE = 12;
    public final int CELL_INSET = 3;
    
    public final Color COLOR_CLOSED = new Color(204, 153, 255); //Color.MAGENTA;
    public final Color COLOR_STEP = new Color(0,204,102);
    public final Color COLOR_DEST = Color.RED;
    public final Color COLOR_WALL = new Color(64, 64, 64);
    
    private final int rowCount;
    private final int colCount;
    
    
    // Base x,y give the upper left corner of the world
    private int baseX = 0;
    private int baseY = 0;
    
    // Last base x,y is the last base x,y after releasing the mouse; this
    // allows relative movements.
    private int lastBaseX;
    private int lastBaseY;
    private int touchX;
    private int touchY;
    private char[][] map;
    private Node src;
    private Node dest;
    private LinkedList<Node> open;
    private LinkedList<Node> closed;
    
    public WorldPanel(char[][] map) {
        this.map = map;
        
        this.rowCount = map.length;
        this.colCount = map[0].length;
        
        init();
    }
    
    public WorldPanel(int rowCount, int colCount) {
        super();
        
        this.rowCount = rowCount;
        this.colCount = colCount;
        
        init();
    }
    
    private void init() {
        addMouseListener(this);
        addMouseMotionListener(this);
        repaint();
    }
    
    public void update(Node src, Node dest, LinkedList<Node> open, LinkedList<Node> closed) {
        this.src = src;
        this.dest = dest;
        this.open = open;
        this.closed = closed;
        
        repaint();
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Render the world without steps        
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < colCount; col++) {
                char tile = map[row][col];

                switch (tile) {
                    case World.PLAYER_START_TILE:
                        // Only paint the src if there isn't a node for it
                        if (src != null) {
                            continue;
                        }

                        g.setColor(COLOR_STEP);
                        break;

                    case World.WALL_TILE:
                        g.setColor(COLOR_WALL);
                        break;

                    case World.GATEWAY_TILE:
                        dest = new Node(row, col);
                        
                        g.setColor(Color.RED);
                        break;

                    case World.NO_TILE:
                    default:
                        g.setColor(Color.WHITE);
                }

                int x = mapToPixelX(col);
                int y = mapToPixelY(row);

                g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                
                if(tile == World.PLAYER_START_TILE || tile == World.GATEWAY_TILE) {
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                }
            }
        }

        if (src == null)
            return;

        // Render the open cells
        g.setColor(Color.ORANGE);
        for (Node node : open) {
            int x = mapToPixelX(node.getX());
            int y = mapToPixelY(node.getY());
            g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
        }

        // Render the closed cells
        g.setColor(COLOR_CLOSED);
        for (Node node : closed) {
            int x = mapToPixelX(node.getX());
            int y = mapToPixelY(node.getY());
            g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
        }
        
        // Render the walk so far -- do this last because the current node
        // has been moved to the closed list.

        Node step = src;
        do {
            int x = mapToPixelX(step.getX());
            int y = mapToPixelY(step.getY());
            
            g.setColor(COLOR_STEP);
            g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
            
            g.setColor(Color.BLACK);
            g.drawRect(x, y, CELL_SIZE, CELL_SIZE);

            // Look back to parent to get how we got here
            step = step.getParent();
        } while (step != null);
        
        // Render the destination last
        int x = mapToPixelX(dest.getX());
        int y = mapToPixelY(dest.getY());
        
        g.setColor(COLOR_DEST);
        g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
        
        g.setColor(Color.BLACK);
        g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
    }
    
    private int mapToPixelX(int coord) {
        int index = coord * (CELL_SIZE + CELL_INSET) + CELL_INSET + baseX;
        
        return index;
    }
    
    private int mapToPixelY(int coord) {
        int index = coord * (CELL_SIZE + CELL_INSET) + CELL_INSET + baseY;
        
        return index;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
//        System.out.println("clicked x = "+x+" y = "+y);
        
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point point = e.getPoint();

        touchX = point.x;
        touchY = point.y;
        
        repaint();        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        lastBaseX = baseX;
        lastBaseY = baseY;
        
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
//        System.out.println("entered x = "+x+" y = "+y);
        
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
//        System.out.println("exited x = "+x+" y = "+y);
        
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point point = e.getPoint();

        int dragToX = point.x;
        int dragToY = point.y;
        
        // Update the base x,y depending on how much we dragged the world
        baseX = lastBaseX + dragToX - touchX;
        baseY = lastBaseY + dragToY - touchY;
        
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
    
}
