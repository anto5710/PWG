package ui.Renderer.fundament;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.Vector;

import board.ADual;
import board.Coord;
import board.Formation;
import board.Team;

public class PerspectiveBoard extends FocusableBoard{
//	private AffineTransform tp = new AffineTransform();
	private Point start;
	
	public double vecx = 0, vecy = 0;
	
	public PerspectiveBoard(ADual game) {
		super(game);
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if(e.isShiftDown()) whenDragged(e);
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(e.isShiftDown()) whenPressed(e);
			}
		});
		
	}
	
	public Point2D applyTransform(Point point){
		return point==null? null: perspective().transform(point, point);
	}
	
	public AffineTransform perspective(){
//		return tp;
		
//		System.out.println(vecx +"   "+ vecy);
		
		
		
		return AffineTransform.getRotateInstance(round(vecx), round(vecy), centerX(), centerY());
//		return AffineTransform.get
	}
	
	private int round(double num){
//		return (int) num;
		return (int) (Math.abs(num) <= 20 ? 0 : num);
	}
	
	private void whenPressed(MouseEvent e){
		start = e.getPoint(); 
	}
	
	private void whenDragged(MouseEvent e){
		if(start==null){
			start = e.getPoint(); return;
		}
		rotate(start, e.getPoint());
		repaint();
	}
	
	public double centerX(){
		return getWidth()/2D;
	}
	
	public double centerY(){
		return getHeight()/2D;
	}
	
	protected double sensitivity = 0.5;
	public void rotate(Point p1, Point p2){
		double dx = p2.getX()-p1.getX();
		double dy = p2.getY()-p1.getY();
		rotate(dx*sensitivity, dy*sensitivity);
	}
	
	public void rotate(double vecx, double vecy){
		this.vecx=vecx;
		this.vecy=vecy;
	}
	
	@Override
	public Coord toCoord(int px, int py) {
		Point2D src = new Point(px, py);
		
		try {
			src = perspective().inverseTransform(src, src);
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
		return super.toCoord((int)src.getX(), (int)src.getY());
	}
	
}
