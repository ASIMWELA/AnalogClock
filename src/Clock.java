//AUGUSTINE SIMWELA
//BSC-49-16


import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import static java.lang.Math.abs;

public class Clock extends JFrame
{
    public Clock()
    {
        this.setSize(800, 600);
        this.setVisible(true);
        this.add(new DrawAnalogClock());
        this.setTitle("Static Clock");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

    }

    public static void main(String[] args)
    {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run()
            {
                new Clock();
            }
        });
    }


    static class DrawAnalogClock extends JPanel
    {
        private static final double ELLIPSE_WIDTH_HEIGHT= 11.0;
        private static final double CENTER_X = 0.2;
        private static final double CENTER_Y= 1.4;

        int width = 800, height=600;
        public DrawAnalogClock()
        {
            this.setSize(width, height);
        }

        @Override
        public void paint(Graphics g)
        {

            Graphics2D g2 = (Graphics2D)g;

            //transform window to ViewPort coordinate systems
            windowToViewPortTransform(g2, -10F, 10F, -10F, 10F, 800F, 600F,true );

            //set default coordinate systems
            g2.setStroke(new BasicStroke(0.05F));

            //set Font size to be less than 1
            Font f = new Font("TimesNewRoman", Font.BOLD, 1);
            g2.setFont(f.deriveFont(0.8F));
            AffineTransform t = g2.getTransform();


            //draw number in the upright
            g2.scale(1, -1);
            drawNumbers(g2);

            // The larger circle
            g2.setTransform(t);
            drawCircle(g2);

            //scale down the circle to be inside the larger circle
            g2.translate(CENTER_X,CENTER_Y);
            g2.scale(0.8, 0.8);
            g2.translate(-CENTER_X,-CENTER_Y);
            drawCircle(g2);

            //set original transform
            g2.setTransform(t);


            //scale down the filled circle an position at the centre
            g2.translate(CENTER_X,CENTER_Y);
            g2.scale(0.02, 0.02);
            g2.translate(-CENTER_X,-CENTER_Y);
            drawFilledCircle(g2);

            //set original transform
            g2.setTransform(t);

            //draw ticks by considering the radius of the inside circle
            drawTickMarks(g2);
            g2.setTransform(t);
            drawHands(g2);

            //set original transform
            g2.setTransform(t);

            //draw the hand and point it at 12 by rotating it by Math.PI/2 and scaling down
            g2.translate(CENTER_X,CENTER_Y);
            g2.rotate(Math.PI/2);
            g2.scale(0.7, 1);
            g2.translate(-CENTER_X,-CENTER_Y);
            drawHands(g2);
        }

        void drawHands(Graphics2D g2)
        {
            g2.setStroke(new BasicStroke(0.005F));
            GeneralPath p = new GeneralPath();

            //draw the hand
            p.moveTo(CENTER_X,CENTER_Y);
            p.lineTo(-CENTER_X, CENTER_Y);
            p.lineTo(-0.6, 1.6);
            p.lineTo(3.6, 1.6);
            p.lineTo(4, CENTER_Y);
            p.lineTo(3.6, 1.2);
            p.lineTo(-0.6,1.2);
            p.lineTo(-CENTER_X,CENTER_Y);
            p.lineTo(4,CENTER_Y);
            g2.draw(p);
        }

        //draw circle (i.e ellipse with the same width and height)
        void drawCircle(Graphics2D g2)
        {
            double newX = CENTER_X - ELLIPSE_WIDTH_HEIGHT / 2.0;
            double newY = CENTER_Y - ELLIPSE_WIDTH_HEIGHT / 2.0;

            Ellipse2D ellipse = new Ellipse2D.Double(newX, newY, ELLIPSE_WIDTH_HEIGHT, ELLIPSE_WIDTH_HEIGHT);

            g2.draw(ellipse);
        }

        //draw a filled circle to be used at the centre
        void drawFilledCircle(Graphics2D g2)
        {
            double newX = CENTER_X - ELLIPSE_WIDTH_HEIGHT / 2.0;
            double newY = CENTER_Y - ELLIPSE_WIDTH_HEIGHT / 2.0;

            Ellipse2D ellipse = new Ellipse2D.Double(newX, newY, ELLIPSE_WIDTH_HEIGHT, ELLIPSE_WIDTH_HEIGHT);
            g2.fill(ellipse);
        }

        //draw tickMarks by considering the radius of the inside circle
        void drawTickMarks(Graphics2D g2)
        {

            GeneralPath ticksPath = new GeneralPath();

            double tickLen = 0.2;  // short tick
            double medTickLen = 0.6;  // at 5-minute intervals

            // Draw a tick for each "second" (1 through 60)
            for ( int i=1; i<= 60; i++)
            {
                Stroke tickStroke = new BasicStroke(0.05F);

                //set default tick length
                double len = tickLen;
                g2.setStroke(tickStroke);
                //target the radius of the inside circle
                double r = 4.3;
                if ( i % 5 == 0 )
                {
                    // Medium ticks on the '5's (every 5 ticks)
                    len = medTickLen;

                    //make the long ticks protrude the outer circle by increasing its radius
                    r = 4.6;

                }

                //convert to double for calculating the angle to move the point to
                double di = (double)i; //

                // Get the angle from 12 O'Clock to this tick (radians)
                double twelveHourAngle = di/60.0*2.0*Math.PI;


                // Get the angle from 3 O'Clock to this tick
                // Note: 3 O'Clock corresponds with zero angle in circle

                double fifteenNthHourAngle = Math.PI/2.0-twelveHourAngle;

                // Move to the outer edge of the circle at correct position
                // for this tick using the angle

                ticksPath.moveTo((float)(CENTER_X + Math.cos(fifteenNthHourAngle)*r), (float)(CENTER_Y - Math.sin(fifteenNthHourAngle)*r));

                // Draw line inward along radius for length of tick mark

                ticksPath.lineTo((float)(CENTER_X + Math.cos(fifteenNthHourAngle)*(r-len)),(float)(CENTER_Y-Math.sin(fifteenNthHourAngle)*(r-len)));
            }

            //draw the ticks
            g2.draw(ticksPath);

        }
        void drawNumbers(Graphics2D g){

            //assigning r slightly bigger to correspond with the just
            // inside the larger circle with radius 11.0/2
            //target exactly just the inside of the larger circle

            double r = 6.1;
            for ( int i=1; i<=12; i++)
            {
                // Calculate the string width and height to center it properly
                double cX = 0.0;
                double cY = 0.0;
                String numStr = ""+i;

                //get the current character width and override using the ith string
                int charWidth = g.getFontMetrics().stringWidth(numStr);

                //leave the current character width
                int charHeight = g.getFontMetrics().getHeight();

                double di = (double) i;

                // Calculate the position along the edge of the clock where the number should
                // be drawn
                // Get the angle from 12 O'Clock to this tick (radians)
                double twelveHourAngle = di / 12.0 * 2.0 * Math.PI;

                // Get the angle from 3 O'Clock to this tick
               //3 O'clock corresponds to angle size of 0 in unit circle and from here
                //the circle can be transversed through counter clock wise

                double fifteenNthHourAngle = Math.PI / 2.0 - twelveHourAngle;


                // Get diff between number position and clock center
                double translateX = (Math.cos(fifteenNthHourAngle) * (r - 1));
                double translateY = (-Math.sin(fifteenNthHourAngle) * (r - 1));


                // Translate the graphics context by delta between clock center and
                // number position and move the numbers in position

                g.translate(translateX, translateY);

                // Draw number at clock center.

                float rt = (float) cX -charWidth/2;
                float rx =(float) cY-charHeight/2;

                g.drawString(numStr, rt ,rx);

                // Undo translation
                g.translate(-translateX, -translateY);

            }
        }

        void windowToViewPortTransform(Graphics2D g2,
                                       float left, float right,   // horizontal limits on view window
                                       float bottom, float top,   // vertical limits on view window
                                       float width, float height, // width and height of viewport
                                       boolean preserveAspect // should window be forced to match viewport aspect?
        )
        {

            if (preserveAspect)
            {
                // Adjust the limits to match the aspect ratio of the drawing area.
                float displayAspect = StrictMath.abs(height / width);
                float windowAspect = abs((top - bottom) / (right - left));

                if (displayAspect > windowAspect)
                {
                    float excess = (top - bottom) * (displayAspect / windowAspect - 1);
                    top = top + excess / 2;
                    bottom = bottom - excess / 2;

                }
                // Expand the viewport vertically.
                else if (displayAspect < windowAspect)
                {
                    float excess = (right - left) * (windowAspect / displayAspect - 1);
                    right = right + excess / 2;
                    left = left - excess / 2;
                }
                // Expand the viewport horizontally.

                g2.scale(width / (right - left), height / (bottom - top));
                g2.translate(-left, -top);
            }
        }
    }

}