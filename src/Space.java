public class Space {


    private double minY;
    private double minX;
    private double height;
    private double width;

    Space(double minY, double minX, double height, double width){
        this.minX = minX;
        this.minY = minY;
        this.height = height;
        this.width = width;
    }

    public double getMinY() {
        return minY;
    }

    public double getMinX() {
        return minX;
    }

    double getMaxY(){
        return minY + height;
    }

    double getMaxX(){
        return minX + width;
    }

}
