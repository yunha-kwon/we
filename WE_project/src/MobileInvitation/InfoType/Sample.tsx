import React, { useEffect, useState } from "react";
import rose from "../../assets/images/rose.png";
import flower_sm from "../../assets/images/flower-sm.png";
import leaf from "../../assets/images/leaf.png";
import pinkpaper from "../../assets/images/pinkpaper.jpeg";
import kakaoicon from "../../assets/images/kakaoicon.png";
import copyicon from "../../assets/images/copyicon.png";
import wedding from "../../assets/images/wedding.jpg";
import InvitationMap from "./InvitationMap";
import Calendar from "react-calendar";
import "react-calendar/dist/Calendar.css";
import "./StorageDetail.css";
import dayjs from "dayjs";
import Swal from "sweetalert2";
import AOS from "aos";
import beige from "../../assets/images/beige.png";
import { Fade } from "react-awesome-reveal";
import FallingSnow from "../../Components/Snowflake";

const Sample: React.FC = () => {
  const markDate = new Date(2024, 9, 26);
  const kakaokey = import.meta.env.VITE_KAKAOMAP_JAVASCRIPT_APP_KEY;
  const [showThumbnail, setShowThumbnail] = useState(true);
  const [fadeClass, setFadeClass] = useState("fade-in");
  const [showSecondFade, setShowSecondFade] = useState(false);

  useEffect(() => {
    const mainTimer = setTimeout(() => {
      setFadeClass("fade-out");

      setTimeout(() => {
        setShowSecondFade(false);
      }, 300);

      setTimeout(() => {
        setShowThumbnail(false);
        setFadeClass("fade-in");
      }, 1000);
    }, 3300);

    return () => clearTimeout(mainTimer);
  }, []);

  useEffect(() => {
    const secondTimer = setTimeout(() => {
      setShowSecondFade(true);
    }, 700);

    return () => clearTimeout(secondTimer);
  }, []);

  useEffect(() => {
    const kakao = (window as any).Kakao;
    if (kakao && !kakao.isInitialized()) {
      kakao.init(kakaokey);
    }
  });

  const handleShare = () => {
    const kakao = (window as any).Kakao;
    if (!kakao) {
      console.error("Kakao SDK not available");
      return;
    }

    if (!kakao.isInitialized()) {
      console.error("Kakao SDK is not initialized");
      return;
    }

    kakao.Link.sendDefault({
      objectType: "feed",
      content: {
        title: `김현빈 ♥ 손예진 결혼합니다`,
        description: `2024년 10월 26일 AM 11시 30분`,
        imageUrl:
          "https://pimg.mk.co.kr/meet/neds/2022/04/image_readtop_2022_293311_16487640614994397.jpg",

        link: {
          mobileWebUrl: window.location.href,
          webUrl: window.location.href,
        },
      },
      buttons: [
        {
          title: "모바일 청첩장 보기",
          link: {
            mobileWebUrl: window.location.href,
            webUrl: window.location.href,
          },
        },
      ],
    });
  };

  const copyToClipboard = () => {
    const url = window.location.href;
    navigator.clipboard
      .writeText(url)
      .then(() => {
        Swal.fire({
          text: "현재 페이지의 링크가 복사되었습니다.",
          icon: "success",
          confirmButtonText: "확인",
          width: "400px",
          customClass: {
            popup: "my-popup-class",
          },
        });
      })
      .catch((err) => {
        Swal.fire({
          title: "복사 실패",
          text: "링크 복사에 실패했습니다. 다시 시도해 주세요.",
          icon: "error",
          confirmButtonText: "확인",
        });
        console.error("복사 실패:", err);
      });
  };
  React.useEffect(() => {
    AOS.init({ duration: 2000 });
  }, []);

  return (
    <div className="relative font-nanum min-w-[1500px] w-full">
      <div
        className="absolute inset-0 bg-cover bg-center"
        style={{
          backgroundImage: `url(${pinkpaper})`,
          opacity: 0.2,
        }}
      />
      <div className="relative z-10 text-center w-full">
        <button className="flex justify-start ml-36 absolute mt-10 bg-transparent">
          <a href="/invitation/storage">
            <p className="text-[#C1A56C]">{"<<"} 뒤로가기</p>
          </a>
        </button>

        <div className="flex justify-center items-center relative z-10 pointer-none">
          {showThumbnail ? (
            <img
              src={beige}
              alt="썸네일"
              className={`w-1/3 h-auto ${fadeClass}`}
            />
          ) : (
            <div
              className="text-2xl font-semibold letter-space pt-36 text-[#800000]"
              data-aos="fade-up"
            >
              WEDDING INVITATION
            </div>
          )}

          {showThumbnail && (
            <div className="absolute inset-0 flex justify-center items-center">
              <div
                className="text-[30px] text-[#C5A88E] text-center "
                style={{ minHeight: "100px" }}
              >
                {showSecondFade && (
                  <div className="letter-space-md">
                    <Fade cascade damping={0.3}>
                      {"현빈 ఇ 예진"}
                    </Fade>
                  </div>
                )}
                {showSecondFade && (
                  <div className="text-[20px] w-full flex justify-center">
                    <Fade direction={"up"} className="slide-up text-center">
                      {"2024.10.26"}
                    </Fade>
                  </div>
                )}
              </div>
            </div>
          )}
        </div>
        {!showThumbnail ? (
          <>
            <div className="letter-space w-full" data-aos="fade-up">
              <p className="text-3xl mt-10 flex justify-center text-[#222B45]">
                김현빈
                <span className="w-8 mx-10">
                  <img src={leaf} alt="장식 이미지" />
                </span>
                손예진
              </p>
            </div>
            <FallingSnow />

            <div
              className="w-full flex justify-center mt-10"
              data-aos="fade-up"
            >
              <img src={wedding} alt="대표 사진" className="w-1/3 h-auto" />
            </div>

            <div className="text-xl">
              <div data-aos="fade-up">
                <p className="mt-10">2024년 10월 26일 토요일 오후 11시 30분</p>
                <p className="mt-5">파스텔웨딩홀, 1층 에메랄드관</p>
              </div>

              <div className="flex justify-center mt-20" data-aos="fade-up">
                <div className="w-[480px] min-h-[400px] bg-[#FDF5E6] mb-20">
                  <div className="flex justify-center">
                    <img src={flower_sm} alt="꽃" className="w-10 mt-5 mb-5" />
                  </div>
                  <p className="text-[#B3B3B3] letter-space-sm mb-5">
                    INVITATION
                  </p>
                  <div className="border border-gray-200 mx-10 mb-10"></div>
                  <div className="text-[16px] mx-24 mb-10">
                    <p>햇살처럼 따뜻하게 안아줄 수 있는</p>
                    <p>늘 곁에서 서로를 웃게 해줄 수 있는</p>
                    <p> 소중한 사람을 만났습니다.</p>
                    <br />

                    <p>햇살 가득한</p>
                    <p>10월 결혼합니다.</p>
                    <br />
                    <p>기쁜 날, 가까이서 축복해 주시면</p>
                    <p>더 없는 기쁨으로 간직하겠습니다.</p>
                  </div>
                  <div className="border border-gray-200 mx-10 mb-10"></div>
                  <div className="text-[16px]">
                    <p>
                      <span className="font-semibold">김윤석ㆍ박미숙</span>
                      <span className="text-[#A88E97]">의 장남 </span>
                      <span className="font-semibold">현빈</span>
                    </p>
                    <p className="mb-10">
                      <span className="font-semibold">손정길ㆍ이혜정</span>
                      <span className="text-[#A88E97]">의 차녀 </span>
                      <span className="font-semibold">예진</span>
                    </p>
                  </div>
                </div>
              </div>

              <div
                className="flex justify-center items-center"
                data-aos="fade-up"
              >
                <div className="w-[480px]">
                  <div className="text-center">
                    <p className="text-[#B3B3B3] letter-space-sm mb-5">
                      CALENDAR
                    </p>
                    <div className="border border-gray-200 mx-10 mb-10"></div>
                    <p className="text-[18px] mb-5">2024년 10월 26일 토요일</p>
                    <div className="flex justify-center mb-20">
                      <Calendar
                        className="detailcalendar"
                        value={markDate}
                        locale="en-US"
                        formatDay={(_, date) => dayjs(date).format("DD")}
                        tileClassName={({ date }) => {
                          if (
                            date.getFullYear() === markDate.getFullYear() &&
                            date.getMonth() === markDate.getMonth() &&
                            date.getDate() === markDate.getDate()
                          ) {
                            return "highlight";
                          }
                          return null;
                        }}
                      />
                    </div>
                  </div>
                </div>
              </div>

              <div className="flex justify-center" data-aos="fade-up">
                <div className="w-[480px]">
                  <div className="text-center">
                    <p className="text-[#B3B3B3] letter-space-sm mb-2">
                      LOCATION
                    </p>
                    <p className="text-[#86626E] text-sm letter-space-sm mb-5">
                      오시는 길
                    </p>
                    <div className="border border-gray-200 mx-10 mb-10"></div>
                    <p className="text-[18px]">서울 중구 동호로 249</p>
                    <p className="mb-10 text-[18px]">
                      파스텔웨딩홀 1층 에메랄드관
                    </p>
                  </div>
                </div>
              </div>

              <div className="flex justify-center" data-aos="fade-up">
                <div className="mb-20 w-[400px]">
                  <InvitationMap
                    latitude={37.5575055053737}
                    longitude={127.007952910656}
                  />
                </div>
              </div>

              <div className="flex justify-center mb-10" data-aos="fade-up">
                <div className="w-[480px] flex justify-center">
                  <img src={rose} alt="장미 한 송이" className="w-10" />
                </div>
              </div>

              <div className="text-[18px]" data-aos="fade-up">
                <p className="text-center">참석이 어려우신 분들은</p>
                <p className="text-center">축하의 마음을 전달해 주세요.</p>
              </div>

              <div className="flex justify-center mt-5" data-aos="fade-up">
                <div className="w-[400px] h-[70px] bg-[#F4F0EB] flex items-center justify-between cursor-pointer p-2">
                  <p className="text-[18px] flex-1 text-center">
                    축의금 계좌번호
                  </p>
                </div>
              </div>
              <div className="flex justify-center" data-aos="fade-up">
                <div className="w-[400px] bg-white border border-gray-200 p-2 mb-20">
                  <p className="text-[15px]">
                    한국은행 0016800061891065
                    <button className="ml-20 bg-white text-gray-800 border border-gray-300 rounded-md shadow-sm hover:shadow-md transition-shadow duration-200 px-2 py-1">
                      이체하기
                    </button>
                  </p>
                </div>
              </div>
            </div>
          </>
        ) : (
          ""
        )}
        {!showThumbnail ? (
          <div className="flex justify-center">
            <div className="w-[560px] h-[100px] bg-[#F4F0EB] flex items-center cursor-pointer p-2 mb-40">
              <div
                className="flex-1 border-r border-gray-300 h-full flex flex-col items-center justify-center"
                onClick={handleShare}
              >
                <img src={kakaoicon} alt="카톡 아이콘" className="w-9 mb-2" />
                <p className="text-sm">카카오톡 공유</p>
              </div>
              <div
                className="border-l border-gray-300 h-full"
                style={{ width: "1px" }}
              ></div>
              <div
                className="flex-1 flex flex-col items-center justify-center"
                onClick={copyToClipboard}
                style={{ cursor: "pointer" }}
              >
                <img src={copyicon} alt="복사 아이콘" className="w-9 mb-2" />
                <p className="text-sm">링크(URL) 복사</p>
              </div>
            </div>
          </div>
        ) : (
          " "
        )}
      </div>
    </div>
  );
};

export default Sample;
