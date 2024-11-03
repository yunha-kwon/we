/* eslint-disable @typescript-eslint/no-explicit-any */
import React, { useEffect, useState } from "react";

declare global {
  interface Window {
    kakao: any;
    daum: any;
  }
}

interface IAddr {
  address: string;
}

interface KakaoMapProps {
  onLocationChange: (
    address: string,
    latitude: number,
    longitude: number
  ) => void;
  address: string;
  latitude: number | null;
  longitude: number | null;
}

const KakaoMap: React.FC<KakaoMapProps> = ({
  onLocationChange,
  address,
  latitude,
  longitude,
}) => {
  const [map, setMap] = useState<any>(null);
  const [marker, setMarker] = useState<any>(null);
  const [showMap, setShowMap] = useState<boolean>(true);

  useEffect(() => {
    if (window.kakao && window.daum && showMap) {
      window.kakao.maps.load(() => {
        const container = document.getElementById("map");
        const options = {
          center:
            latitude && longitude
              ? new window.kakao.maps.LatLng(latitude, longitude)
              : new window.kakao.maps.LatLng(33.450701, 126.570667),
          level: 4,
        };

        const mapInstance = new window.kakao.maps.Map(container, options);
        const markerInstance = new window.kakao.maps.Marker();
        setMap(mapInstance);
        setMarker(markerInstance);

        if (latitude && longitude) {
          const currentPos = new window.kakao.maps.LatLng(latitude, longitude);
          mapInstance.setCenter(currentPos);
          markerInstance.setPosition(currentPos);
          markerInstance.setMap(mapInstance);
        } else if (navigator.geolocation) {
          navigator.geolocation.getCurrentPosition(
            (position) => {
              const currentPos = new window.kakao.maps.LatLng(
                position.coords.latitude,
                position.coords.longitude
              );

              mapInstance.setCenter(currentPos);
              markerInstance.setPosition(currentPos);
              markerInstance.setMap(mapInstance);
            },
            () => {
              console.error("Could not get current location.");
            }
          );
        } else {
          console.error("Geolocation is not supported by this browser.");
        }
      });
    } else if (!showMap) {
      setMap(null);
    }
  }, [showMap, latitude, longitude]);

  useEffect(() => {
    if (map && marker && latitude && longitude) {
      const currentPos = new window.kakao.maps.LatLng(latitude, longitude);
      map.panTo(currentPos);
      marker.setPosition(currentPos);
      marker.setMap(map);
    }
  }, [map, marker, latitude, longitude]);

  const onClickAddr = () => {
    if (window.daum && window.kakao) {
      new window.daum.Postcode({
        oncomplete: function (addrData: IAddr) {
          const geocoder = new window.kakao.maps.services.Geocoder();

          geocoder.addressSearch(
            addrData.address,
            function (result: any, status: any) {
              if (status === window.kakao.maps.services.Status.OK) {
                const currentPos = new window.kakao.maps.LatLng(
                  result[0].y,
                  result[0].x
                );

                (document.getElementById("addr") as HTMLInputElement).value =
                  addrData.address;

                map.panTo(currentPos);
                marker.setMap(null);
                marker.setPosition(currentPos);
                marker.setMap(map);

                onLocationChange(addrData.address, result[0].y, result[0].x);
              } else {
                alert("주소를 찾을 수 없습니다.");
              }
            }
          );
        },
      }).open();
    } else {
      alert("카카오 주소 검색 API가 로드되지 않았습니다.");
    }
  };

  const handleCheckboxChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setShowMap(event.target.checked);
  };

  return (
    <div className="w-full">
      <div className="flex justify-between">
        <input
          id="addr"
          readOnly
          className="border border-gray-400 w-72 h-10 text-center"
          placeholder="주소를 입력하세요"
          defaultValue={address}
        />
        <button
          onClick={onClickAddr}
          className="py-2 w-24 rounded-md text-md bg-[#FFD0DE] text-sm"
        >
          검색
        </button>
      </div>

      <div className="mt-4">
        <label>
          <input
            type="checkbox"
            checked={showMap}
            onChange={handleCheckboxChange}
          />{" "}
          지도 표시
        </label>
      </div>

      {showMap && (
        <div
          id="map"
          style={{ width: "400px", height: "300px", marginTop: "20px" }}
        ></div>
      )}
    </div>
  );
};

export default KakaoMap;
