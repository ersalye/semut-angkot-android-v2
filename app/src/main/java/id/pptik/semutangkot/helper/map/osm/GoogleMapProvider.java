package id.pptik.semutangkot.helper.map.osm;

import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.tileprovider.tilesource.XYTileSource;

import java.util.Locale;

public class GoogleMapProvider extends XYTileSource {
    public static String[] STANDARD = new String[] {
            "http://mt0.google.com/vt/lyrs=m&hl={hl}&x={x}&y={y}&z={z}&s=Galileo",
            "http://mt1.google.com/vt/lyrs=m&hl={hl}&x={x}&y={y}&z={z}&s=Galileo",
            "http://mt2.google.com/vt/lyrs=m&hl={hl}&x={x}&y={y}&z={z}&s=Galileo",
            "http://mt3.google.com/vt/lyrs=m&hl={hl}&x={x}&y={y}&z={z}&s=Galileo"
    };

    //http://mt1.google.com/vt/lyrs=y&x=1325&y=3143&z=13
    //http://khm0.google.com/kh/v=165&hl=en&x=18&y=9&z=5&s=Galileo
    public static String[] SATELLITE = new String[] {
            "http://mt0.google.com/vt/lyrs=y&hl={hl}&x={x}&y={y}&z={z}&s=Galileo",
            "http://mt1.google.com/vt/lyrs=y&hl={hl}&x={x}&y={y}&z={z}&s=Galileo",
            "http://mt2.google.com/vt/lyrs=y&hl={hl}&x={x}&y={y}&z={z}&s=Galileo",
            "http://mt3.google.com/vt/lyrs=y&hl={hl}&x={x}&y={y}&z={z}&s=Galileo"
    };

    public static String[] TRAFFIC = new String[] {
            "http://mt0.google.com/vt?lyrs=traffic&hl={hl}&x={x}&y={y}&z={z}&style=5",
            "http://mt1.google.com/vt?lyrs=traffic&hl={hl}&x={x}&y={y}&z={z}&style=5",
            "http://mt2.google.com/vt?lyrs=traffic&hl={hl}&x={x}&y={y}&z={z}&style=5",
            "http://mt3.google.com/vt?lyrs=traffic&hl={hl}&x={x}&y={y}&z={z}&style=5"
    };

    // https://mts1.google.com/vt?lyrs=h&hl=x-local&src=app&x=1325&y=3143&z=13&s=Galile
    public static String[] HYBRID = new String[] {
            "http://mt0.google.com/vt?lyrs=h&hl={hl}&x={x}&y={y}&z={z}&s=Galileo",
            "http://mt1.google.com/vt?lyrs=h&hl={hl}&x={x}&y={y}&z={z}&s=Galileo",
            "http://mt2.google.com/vt?lyrs=h&hl={hl}&x={x}&y={y}&z={z}&s=Galileo",
            "http://mt3.google.com/vt?lyrs=h&hl={hl}&x={x}&y={y}&z={z}&s=Galileo"
    };

    public GoogleMapProvider(String name, String[] baseUrls) {
        super(name, 0, 23, 256, "png", baseUrls);
    }

    @Override
    public String getTileURLString(MapTile aTile) {
        return getBaseUrl()
                .replace("{x}", Integer.toString(aTile.getX()))
                .replace("{y}", Integer.toString(aTile.getY()))
                .replace("{z}", Integer.toString(aTile.getZoomLevel()))
                .replace("{hl}", Locale.getDefault().getLanguage());
    }
}