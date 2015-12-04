package client.customUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JButton;

public class PaintedButton extends JButton
{
	Image image;
	
	public PaintedButton(Dimension d, Image img)
	{
		image = img;
		setPreferredSize(d);
		
		setOpaque(true);
		setBackground(new Color(0,0,0,0));
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		super.paintComponent(g);
	}
}
