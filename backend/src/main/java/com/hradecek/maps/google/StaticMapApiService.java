package com.hradecek.maps.google;

import com.hradecek.maps.types.LatLng;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;
import javax.imageio.ImageIO;

import com.google.maps.GeoApiContext;
import com.google.maps.StaticMapApi;
import com.google.maps.StyledMap;
import com.google.maps.errors.ApiException;

import static com.google.maps.StyledMap.Feature;
import static com.google.maps.StyledMap.Rule;

/**
 * Simple service for Static Map API.
 */
public class StaticMapApiService extends MapsApi {

    /**
     * Constructor
     *
     * @param context GeoApiContext
     */
    public StaticMapApiService(final GeoApiContext context) {
        super(context);
    }

    public Boolean isWater(final LatLng center) {
        final var rules = Map.of(Rule.COLOR, "0x000000");
        final var style = new StyledMap(Feature.WATER, rules);
        try {
            var result = StaticMapApi.getStaticMap(context, Utils.toGLatLng(center), 1, 1, 21).style(style).await();
            return isBlack(toBufferedImage(result.imageData));
        } catch (InterruptedException | IOException | ApiException ex) {
            throw new StaticMapApiException(ex);
        }
    }

    private static BufferedImage toBufferedImage(byte[] imageData) throws IOException {
        return ImageIO.read(new ByteArrayInputStream(imageData));
    }

    private static boolean isBlack(final BufferedImage image) {
        int color =  image.getRGB(0, 0);
        int red = color & 0x00ff0000 >> 16;
        int green = color & 0x0000ff00 >> 8;
        int blue = color & 0x000000ff;

        return red == 0 && green == 0 && blue == 0;
    }
}
