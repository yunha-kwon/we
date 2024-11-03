import { useEffect } from "react";

interface KakaoMapProps {
  latitude: number;
  longitude: number;
}

const InvitationMap: React.FC<KakaoMapProps> = ({ latitude, longitude }) => {
  useEffect(() => {
    const { kakao } = window;

    const container = document.getElementById("map");
    const options = {
      center: new kakao.maps.LatLng(latitude, longitude),
      level: 3,
    };

    const map = new kakao.maps.Map(container, options);

    const markerPosition = new kakao.maps.LatLng(latitude, longitude);
    const marker = new kakao.maps.Marker({
      position: markerPosition,
    });

    marker.setMap(map);
  }, [latitude, longitude]);

  return <div id="map" style={{ width: "100%", height: "400px" }} />;
};

export default InvitationMap;
