package co.uk.ticklethepanda.location.history.cartograph;

public class GeodeticData<S extends Point, T> {

    private S point;
    private T data;

    public GeodeticData(S point, T data) {
        this.point = point;
        this.data = data;
    }

    public S getPoint() {
        return point;
    }

    public T getData() {
        return data;
    }

}
