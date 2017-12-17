package com.hradecek.maps.google;

import com.google.maps.GeoApiContext;
import com.google.maps.StaticMapApi;
import com.google.maps.StyledMap;
import com.google.maps.StyledMap.Feature;
import com.google.maps.StyledMap.Rule;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import com.google.maps.model.PhotoResult;
import io.reactivex.Single;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple service for Static Map API.
 *
 * @author <a href="mailto:ivohradek@gmail.com">Ivo Hradek</a>
 */
public class StaticMapApiService extends MapApi {

    /**
     * Logger
     */
    private static Logger logger = LoggerFactory.getLogger(StaticMapApiService.class);

    /**
     * Constructor
     *
     * @param context GeoApiContext
     */
    public StaticMapApiService(GeoApiContext context) {
        super(context);
    }

    public Boolean isWater(LatLng center) throws InterruptedException, ApiException, IOException {
        Map<Rule, String> rules = new HashMap<Rule, String>() {{ put(Rule.COLOR, "0x000000"); }};
        StyledMap style = new StyledMap(Feature.WATER, rules);
        PhotoResult result = StaticMapApi.getStaticMap(context, center, 1, 1, 21).style(style).await();

        return isBlack(toBufferedImage(result.imageData));
    }

    private BufferedImage toBufferedImage(byte[] imageData) throws IOException {
        return ImageIO.read(new ByteArrayInputStream(imageData));
    }

    private boolean isBlack(BufferedImage image) {
        int color =  image.getRGB(0, 0);
        int red = color & 0x00ff0000 >> 16;
        int green = color & 0x0000ff00 >> 8;
        int blue = color & 0x000000ff;

        return red == 0 && green == 0 && blue == 0;
    }
}
