/*
Copyright (c) 2021 - 2022, Juan Carlos Garcia Ojeda, Universidad de Cartagena
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution.
   
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package co.edu.unicartagena.b2pstudio.window;

import co.edu.unicartagena.b2pstudio.draw.ArrowHead;
import co.edu.unicartagena.b2pstudio.window.actions.AddArc;
import co.edu.unicartagena.b2pstudio.window.actions.AddNode;
import co.edu.unicartagena.b2pstudio.window.actions.ModifyArc;
import co.edu.unicartagena.b2pstudio.window.actions.ModifyNode;
import co.edu.unicartagena.b2pstudio.objects.Arc;
import co.edu.unicartagena.b2pstudio.objects.Node;
import co.edu.unicartagena.b2pstudio.objects.Canva;
import co.edu.unicartagena.b2pstudio.draw.PointArcs;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class DrawingPanel extends JPanel implements MouseMotionListener, MouseListener {
    private Canva project;

    private Point p1;
    private Point p2;

    private int mode = 1;

    private int diameter = 80;

    private String source;
    private String destination;
    
    private boolean middlepoint =false;
    private boolean dragged =false;
    
    private String ArcSource = "";
    private String ArcDestination = "";
    private Arc arcP;
        
    private int middlePX=0;
    private int middlePY=0;
    
    private int fromPX=0;
    private int toPX=0;
    private int fromPY=0;
    private int toPY=0;
    
    private boolean grid=false;    
    // constructor
    public DrawingPanel() {
        addMouseMotionListener(this);
        addMouseListener(this);
        setLayout(new BorderLayout());
        setBackground(Color.white);
        // PANEL SIZE
        setPreferredSize(new Dimension(1500,3000));
    }

    public void setProject(Canva project) {
        this.project = project;
        repaint();
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
        p1 = null;
        repaint();
    }

    // Metodo para pintar el project
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.LIGHT_GRAY);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.black);
        g2d.setStroke(new BasicStroke(2));
        
        
        if(grid){
            //GUIDELINES
            for (int i = 0; i < 250; i++) {
                g.drawLine(i * 18 + 1, 0, i * 18 + 1, 6000);
                g.drawLine(0, i * 18 + 1, 7200, i * 18 + 1);
            }
        }

        try{
            
            /* UPDATE NEW VERSION */
            /*
            if(middlepoint){
                g.setColor(Color.BLACK);
                System.out.println("Puntos "+p1.x+" "+p1.y);
                int[] xSin = {fromPX,p1.x,toPX};
                int[] ySin = {fromPY,p1.y,toPY};
                g.drawPolyline(xSin, ySin, xSin.length);
            }*/
            
            // ciclo para pintar todos los arcs
            for (Arc arc : project.getArcs()) {
                Node source = project.searchNode(arc.getSource());
                Node destination = project.searchNode(arc.getDestination());
                g.setColor(Color.black);
                // ARC
                // EXTERNAL HELP
                // https://stackoverflow.com/questions/47369565/connect-two-circles-with-a-line
                angleBetween(source, destination);
                
                double from = angleBetween(source, destination);
                double to = angleBetween(destination, source);
                
                PointArcs pointFrom = getPointOnCircle(new PointArcs(source.getX(), source.getY()), from,40);
                PointArcs pointTo = getPointOnCircle(new PointArcs(destination.getX(), destination.getY()), to,40);

                g2d.drawLine((int)pointFrom.getX(), (int)pointFrom.getY(), (int)pointTo.getX(),(int)pointTo.getY());
            
                
                /* UPDATE NEW VERSION */
                if(middlepoint){
                    //look for the middle point
                    if(arc.getId()==arcP.getId() && arcP.getXmiddle()==0){
                        Node s = project.searchNode(ArcSource);
                        Node d = project.searchNode(ArcDestination);
                        //System.out.println("||");
                        //System.out.println("X"+(((int)pointFrom.getX()+(int)pointTo.getX())/2-5));
                        //System.out.println("Y"+(((int)pointFrom.getY()+(int)pointTo.getY())/2-5));                   

                        fromPX=(int)pointFrom.getX();
                        fromPY=(int)pointFrom.getY();
                        
                        toPX=(int)pointTo.getX();
                        toPY=(int)pointTo.getY();
                        
                        middlePX=(((int)pointFrom.getX()+(int)pointTo.getX())/2-5);
                        middlePY=(((int)pointFrom.getY()+(int)pointTo.getY())/2-5);
                        //SET UP ORIENTATION

                        if((int)pointFrom.getX()>(int)pointTo.getX()){
                            g.setColor(Color.red);
                            g.fillRect(((int)pointFrom.getX()+ (int)pointTo.getX())/2-5, ((int)pointFrom.getY()+(int)pointTo.getY())/2-5, 10, 10);
                        }else{
                            g.setColor(Color.red);
                            g.fillRect(((int)pointFrom.getX()+ (int)pointTo.getX())/2-5, ((int)pointFrom.getY()+(int)pointTo.getY())/2-5, 10, 10);
                        }

                    }
                }
                /* */
                
                //DRAW ARROW
                ArrowHead arrowHead = new ArrowHead();
                AffineTransform at = AffineTransform.getTranslateInstance(
                                     pointTo.getX() - (arrowHead.getBounds2D().getWidth() / 2d), 
                                     pointTo.getY());
                at.rotate(from, arrowHead.getBounds2D().getCenterX(), 0);
                arrowHead.transform(at);
                g2d.draw(arrowHead);

                /* END DRAW ARROW*/

                Point pMedio = new Point((source.getX() + destination.getX()) / 2, (source.getY() + destination.getY()) / 2);

                g.setColor(Color.blue);
                g.setFont(new Font("Default",Font.BOLD, 15));
                if(destination.getY()>=source.getY())
                    g.drawString("(" + arc.getTravel_time()+ "," + arc.getCapacity()+ "," + arc.getId() + ")", pMedio.x+10, pMedio.y-10);
                else 
                if(source.getX()<=destination.getX())
                    g.drawString("(" + arc.getTravel_time()+ "," + arc.getCapacity()+ "," + arc.getId() + ")", pMedio.x-10, pMedio.y-10);
            }
            
            
        } catch (Exception e) {
        }

        try {
            for (Node n : project.getNodes()) {
                if(n.getInitial_content() > 0){
                    g.setColor(Color.RED);
                } else if (n.getInitial_content() == 0 && n.getCapacity()<=200 ){
                    g.setColor(Color.YELLOW);
                } else if (n.getCapacity()> 200){
                    g.setColor(Color.BLUE);
                } 
                
                int esc = diameter;
                
                //CAMBIO ESC/2
                //MODIFICAR ELEMENTO
                if(n.getCapacity()> 200){
                    g.fillRect((n.getX() - esc/2), n.getY() - esc/2, esc+25, esc);
                    g.setColor(Color.black);
                    g.drawRect((n.getX() - esc/2), n.getY() - esc/2, esc+25, esc);
                    this.setPreferredSize(new Dimension(1500,3000));
                    this.revalidate();
                }
                else{
                    g.fillOval(n.getX() - esc/2, n.getY() - esc/2, esc, esc);
                    g.setColor(Color.black);
                    g.drawOval(n.getX() - esc/2, n.getY() - esc/2, esc, esc);
                }

                g.setFont(new Font("Default", 0, 30));
                g.drawString(n.getName(), n.getX() - 16, n.getY() + 8);

                g.setFont(new Font("Default", Font.BOLD, 15));
                g.drawString("(" +n.getInitial_content()+ "," + n.getCapacity()+ ")", n.getX() - 20, n.getY() + 30);
            }
        } catch (Exception e) {
        }

        if (p1 != null) {
            if (mode == 1) {
                int esc = diameter;
                g.setColor(Color.yellow);
                g.fillOval(p1.x - esc/2, p1.y - esc/2, esc, esc);
                g.setColor(Color.black);
                g.drawOval(p1.x - esc/2, p1.y - esc/2, esc, esc);

            } else if (mode == 2) {

                if (p2 != null) {
                    g.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
            }
        }
    }
    
    @Override
    public void mouseDragged(MouseEvent me) {
        if (mode == 3) {
            Node n = project.searchNode(me.getPoint());
            if (n != null) {
                p1 = me.getPoint();
                project.modifyNode(n, p1);
                repaint();
            }
        }
        
        /* UPDATE NEW VERSION */
        if(middlepoint){
            if((me.getPoint().x>=middlePX && me.getPoint().x<=middlePX+5) ||
                (me.getPoint().y>=middlePY && me.getPoint().y<=middlePY+5)){
                System.out.println("Movement "+me.getPoint().x+" "+me.getPoint().y);
                System.out.println(middlePX+" "+middlePY);
                //System.out.println("Trying to move element");
                p1=me.getPoint();
                repaint();
                //middlepoint=false;
            }
        }
        /**/
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        if (mode == 1) {
            p1 = me.getPoint();
            repaint();
        } else if (mode == 2) {
             if (p1 != null) {
                p2 = me.getPoint();
                repaint();
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        Arc a = project.searchArc(me.getPoint());
        if (a != null) {
            System.out.println(a.getSource()+" "+a.getDestination());
            middlepoint=true;
            ArcSource=a.getSource();
            ArcDestination=a.getDestination();
            arcP=a;
            p1=me.getPoint();
            repaint();
        }
        else{
            middlepoint=false;
            repaint();
        }
    
        if (mode == 1) {
            p1 = me.getPoint();

            // si no hay un nodo en el punto p1 agregamos el nuevo nodo
            if (project.searchNode(p1) == null) {
                AddNode vent = new AddNode(project, p1);
                vent.setVisible(true);

                mode = 0;
            }
        } else if (mode == 2) {
            Node n = project.searchNode(me.getPoint());
            if (n != null) {
                // si p1 es igual a nulo guardamos el primer punto
                if (p1 == null) {
                    source = n.getName();
                    p1 = me.getPoint();
                } else {      // si no, significa que ya hay un nodo seleccionado
                              // y con segundo punto terminamos de crear el arc
                    
                    destination = n.getName();
                    p2 = me.getPoint();

                    AddArc vent = new AddArc(project, source, destination);
                    vent.setVisible(true);

                    mode = 0;
                }
            }
        } else if (mode == 4) {

            Node n = project.searchNode(me.getPoint());
            if (n != null) {
                ModifyNode vent = new ModifyNode(project, n.getName());
                vent.setVisible(true);

                mode = 0;
            } else {
                a = project.searchArc(me.getPoint());
                if (a != null) {

                    ModifyArc vent = new ModifyArc(project, a.getId());
                    vent.setVisible(true);

                    mode = 0;
                }
            }
        } else if (mode == 5) {

            Node n = project.searchNode(me.getPoint());
            if (n != null) {

                int resp = JOptionPane.showConfirmDialog(null, "¿Do you want to delete this node?",
                        "Delete node", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);

                if (resp == 0) {
                    project.deleteNode(n);
                    repaint();
                }
            } else {
                a = project.searchArc(me.getPoint());
                if (a != null) {

                    int resp = JOptionPane.showConfirmDialog(null, "¿Do you want to delete this arc?",
                            "Delete arc", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);

                    if (resp == 0) {
                        project.deleteArc(a);
                        repaint();
                    }
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }
    
    private double angleBetween(Node source, Node destination){
        int     p1s_x = source.getX();
        int p1s_y = source.getY();
                
        int p2d_x = destination.getX();
        int p2d_y = destination.getY();
                
        //DELTA
                
        int deltaX = p2d_x - p1s_x;
        int deltaY = p2d_y - p1s_y;
                
        //ROTATION
                
        double rotation = -Math.atan2(deltaX, deltaY);
        rotation = Math.toRadians(Math.toDegrees(rotation) + 180);
        return rotation;
    }
    
    private PointArcs getPointOnCircle(PointArcs center, double radians, double radius) {

        double x = center.getX();
        double y = center.getY();

        radians = radians - Math.toRadians(90.0); // 0 becomes the top
        // Calculate the outter point of the line
        double xPosy = Math.round((float) (x + Math.cos(radians) * radius));
        double yPosy = Math.round((float) (y + Math.sin(radians) * radius));

        return new PointArcs(xPosy,yPosy);
    }
    
    public void removeNodes_Arcs(Canva c){
        this.project=null;
        this.removeAll();
        this.revalidate();
        this.repaint();
        this.project=c;
        this.revalidate();
        this.repaint();
    }
    
    public void setGrid(boolean grid){
        this.grid=grid;
        this.repaint();
    }   
}
