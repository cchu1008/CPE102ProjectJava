import java.lang.Math;

public class CartesianPoint
   implements Point
   {
	   private double x_coord;
	   private double y_coord;
	   
	   public CartesianPoint(double x_coord, double y_coord)
	   {
		   this.x_coord = x_coord;
		   this.y_coord = y_coord;
	   }
	   
	   public double xCoordinate()
	   {
		   return this.x_coord;
	   }
	   
	   public double yCoordinate()
	   {
		   return this.y_coord;
	   }
	   
	   public double radius()
	   {
		   double dist = Math.sqrt(Math.pow(this.xCoordinate(), 2) + Math.pow(this.yCoordinate(), 2));
		   return dist;
	   }
	   
	   public double angle()
	   {
		   double fin = Math.atan2(this.yCoordinate(), this.xCoordinate());
		   return fin;
	   }
	   
	   public Point rotate90()
	   {
		   double org_angle = this.angle();
		   double fin_angle = ((this.angle() + Math.toRadians(90)) % Math.toRadians(360));
		   double fin_x = Math.cos(fin_angle) * this.radius();
		   double fin_y = Math.sin(fin_angle) * this.radius();
		   Point fin = new CartesianPoint(fin_x, fin_y);
		   
		   return fin;
	   }
   }