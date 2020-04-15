package com.hradecek.maps.google;

import com.hradecek.maps.types.LatLng;
import com.hradecek.maps.types.Route;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import com.google.maps.GeoApiContext;
import io.reactivex.Single;

/**
 * Simple service for Directions API.
 */
public class DirectionsApiService extends MapApi {

    /**
     * Logger
     */
    private static Logger LOGGER = LoggerFactory.getLogger(DirectionsApiService.class);

    /**
     * Constructor
     *
     * @param context GeoApiContext
     */
    public DirectionsApiService(GeoApiContext context) {
        super(context);
    }

    /**
     * Get route from start location to end destination form of encoded polyline.
     * If there's no route error is emitted.
     *
     * @param origin start location
     * @param destination end location
     * @return Encoded polyline
     */
    private static final String MOCK_ENCODED = "w`|tKwg`|C~BdKOnIg@`DeAbG}AkE{EgLoAa@gBf@{AcCYq@sDjDkDrFgCzGaIhV_GpLeQlUaI`QwNfc@kJbUyEdR}Ef_@iEv^iHz_@sQph@aKrS_@^AbB_Mhb@iHhTyG`MiMzLoFtBiH|@gVdAaHvB}H|E}Zr]aQlP{_Afy@ok@xg@_MtO{KjR_Vjf@u]jb@aPvRqNxVgO|d@yGvTaHjOwNvSkHhNoH~UoTnx@kQpf@oNdb@sIbc@aR~}@wIpVaMpVqRje@_Qh\\qNfWqLz_@wKdg@}I~a@}F|`@kIln@iKne@kR|v@}H|y@eBtP}EdX{Z~_AlSzuCjEvv@LtYuEdxAaAtpB_DtvAVti@zBha@tClWlHr`@rFd^xDhb@lClo@Pfq@GtLz@jk@zC`ZzFhVpJ`d@lVtpB~Ghp@pCnl@@dzAhB~dDiBrj@aHzu@iIdz@uBtg@Rbm@dBd^~Edb@|Khd@`J|\\|Dh]`ApYE~k@Ktx@rD~o@bNbz@fHni@tAbTbAjm@eClz@uE`b@_Kxc@gRhe@qEdOwC|QiFfnAaEti@cKp~@Y~V~@vy@{EbrAeDbtBFvnB_Abt@iSx{CqGngAqNht@iGp]qBlVy@`\\`Ctz@jDrrArCl]jMpt@lFt^~Cv^vBdk@Jzl@{Ane@wClb@sAd`@Jvd@OtkAnAts@jDz[bFbVfI`g@`C`c@PhiA`Bto@hCz_Am@`Z_D|_@yCdRsHfX_Sf^eTx]s^vl@yFzM_IlZiCpOaEzb@}Dv{@iDxYgPtp@_T|r@wHbQqMzQqIxF_GbB_GCsJaBuOIcFhAoH|DyJfIcN`J{JtCsEbBuLfIk_@l]uh@ff@_V|VyExGkFvK_ErKeFrUoE|e@y@~jADnzAr@lnA|Cfg@rZlkCdFrs@vAxm@?|g@_Bh|@oEx{@aFpd@wQjbAuR`bAqM~a@}\\nu@uN~PoJhF{N~N_L|UqEtPkHjc@kM`cAkMh}@gNfj@wL~[kItY{UxqAuShn@gJfg@qHxgAae@xoD}Hb~@gBxm@gBlvBmFzn@cKte@cPpj@yKfe@qJzZcNr[mQvXk_@~f@gZpd@udAreCySfh@uId\\eU~gByDph@}@d[Fjv@jB`i@lA`h@]tY_Ejl@eExc@eSbwBsB~h@h@d`AvDrv@pApl@Bj^hA`|@rGv|@~VfiBb`@dgCdFzk@~Eh{@tBje@Mfl@}FdbAyAzaAgA~oAvKfFrK`CjFo@fOuExJjB|D`DjJdTdHdRhPjc@|Lx]`FfKbD~C";

    public Single<Route> getRoute(LatLng origin, LatLng destination) {
//        return Single.create(singleEmitter -> DirectionsApi.getDirections(context,
//                toGLatLng(origin).toString(), toGLatLng(destination).toString()).setCallback(
//                new PendingResult.Callback<>() {
//                    @Override
//                    public void onResult(DirectionsResult result) {
//                        if (result.routes == null || result.routes.length == 0) {
//                            singleEmitter.onError(n);
//                        }
//                        singleEmitter.onSuccess(new Route(result.routes[0].overviewPolyline.getEncodedPath()));
//                    }
//
//                    @Override
//                    public void onFailure(Throwable throwable) {
//                        final String errorMessage = "No route has been found from " + origin + " to " + destination;
//                        logger.error(errorMessage);
//                        singleEmitter.onError(new DirectionsApiException(errorMessage));
//                    }
//                })
//        );
        return Single.create(singleEmitter -> {
            singleEmitter.onSuccess(new Route(MOCK_ENCODED));
        });
    }
}
