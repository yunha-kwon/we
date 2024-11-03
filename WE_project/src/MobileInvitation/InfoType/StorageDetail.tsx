import React, { useEffect, useState } from "react";
import {
  getFormalInvitation,
  GetFormalInvitationDto,
} from "../../apis/api/getinfotypeinvitation";
import { useParams, useNavigate } from "react-router-dom";
import flower_sm from "../../assets/images/flower-sm.png";
import leaf from "../../assets/images/leaf.png";
import rose from "../../assets/images/rose.png";
import pinkpaper from "../../assets/images/pinkpaper.jpeg";
import kakaoicon from "../../assets/images/kakaoicon.png";
import copyicon from "../../assets/images/copyicon.png";
import InvitationMap from "./InvitationMap";
import Swal from "sweetalert2";
import AOS from "aos";
import Calendar from "react-calendar";
import "react-calendar/dist/Calendar.css";
import "./StorageDetail.css";
import dayjs from "dayjs";
import { deleteFormalInvitation } from "../../apis/api/deleteinfotypeinvitation";
import FallingSnow from "../../Components/Snowflake";
import beige from "../../assets/images/beige.png";
import { Fade } from "react-awesome-reveal";

const StorageDetail: React.FC = () => {
  const [invitationData, setInvitationData] =
    useState<GetFormalInvitationDto | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const { invitationId } = useParams<{ invitationId: string }>();
  const [isExist, setisExist] = useState<boolean>(false);
  const [showThumbnail, setShowThumbnail] = useState(true);
  const [fadeClass, setFadeClass] = useState("fade-in");
  const [showSecondFade, setShowSecondFade] = useState(false);
  const accessToken = localStorage.getItem("accessToken");
  const [isButtonEnabled, setIsButtonEnabled] = useState(false);

  useEffect(() => {
    const userAgent = navigator.userAgent;
    if (/NAVER|KAKAO|WE/i.test(userAgent)) {
      setIsButtonEnabled(true);
    } else {
      setIsButtonEnabled(false);
    }
  }, []);

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

  React.useEffect(() => {
    AOS.init({ duration: 2000 });
  }, []);

  const navigate = useNavigate();
  const kakaokey = import.meta.env.VITE_KAKAOMAP_JAVASCRIPT_APP_KEY;

  const parseDateString = (dateString: string): Date => {
    const parts = dateString.match(/(\d{4})년 (\d{1,2})월 (\d{1,2})일/);
    if (parts) {
      const year = parseInt(parts[1], 10);
      const month = parseInt(parts[2], 10) - 1;
      const day = parseInt(parts[3], 10);
      return new Date(year, month, day);
    }
    return new Date();
  };

  useEffect(() => {
    const secondTimer = setTimeout(() => {
      setShowSecondFade(true);
    }, 700);

    return () => clearTimeout(secondTimer);
  }, []);

  useEffect(() => {
    const fetchInvitation = async () => {
      try {
        const data = await getFormalInvitation(Number(invitationId));
        setInvitationData(data);
        setLoading(false);
      } catch (error) {
        console.error("정보형 청첩장 조회 중 오류 발생:", error);
        throw error;
      }
    };

    fetchInvitation();
  }, [invitationId]);

  useEffect(() => {
    if (accessToken) {
      setisExist(true);
    }
  }, [accessToken]);

  useEffect(() => {
    const kakao = (window as any).Kakao;
    if (kakao && !kakao.isInitialized()) {
      kakao.init(kakaokey);
    }
  });

  if (loading) {
    return <div>Loading...</div>;
  }

  if (!invitationData) {
    return <div>No invitation data available</div>;
  }

  const groomBirthOrderLabel =
    invitationData.groomBirthOrder === "FIRST"
      ? "장남"
      : invitationData.groomBirthOrder === "SECOND"
      ? "차남"
      : "아들";

  const brideBirthOrderLabel =
    invitationData.brideBirthOrder === "FIRST"
      ? "장녀"
      : invitationData.brideBirthOrder === "SECOND"
      ? "차녀"
      : "딸";

  const markDate = parseDateString(invitationData.date);

  const handleEdit = () => {
    navigate(`/invite/info/edit/${invitationId}`);
  };

  const handleDelete = async () => {
    const confirmDelete = window.confirm("정말 삭제하시겠습니까?");
    if (confirmDelete) {
      try {
        await deleteFormalInvitation(Number(invitationId));
        navigate("/invitation/storage");
      } catch (error) {
        console.error("청첩장 삭제 중 오류 발생:", error);
        alert("삭제에 실패했습니다.");
      }
    }
  };

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
        title: `${invitationData.groomLastName}${invitationData.groomFirstName} ♥ ${invitationData.brideLastName}${invitationData.brideFirstName} 결혼합니다`,
        description: `${invitationData.date}${" "}${
          invitationData.timezone
        }${" "}${invitationData.hour}시${" "}${invitationData.minute}분`,
        imageUrl: invitationData.url || "",
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

  function shareDeepLink() {
    const deepLinkUrl = `we://transfer?id=${invitationData?.ledgerId}`;
    window.location.href = deepLinkUrl;
  }

  return (
    <div className="relative font-nanum w-screen">
      <div
        className="absolute inset-0 bg-cover bg-center"
        style={{
          backgroundImage: `url(${pinkpaper})`,
          opacity: 0.2,
        }}
      />
      <div className="relative z-30 text-center w-full pointer-auto">
        {isExist ? (
          <button className="flex justify-start ml-20 absolute mt-10 z-40 pointer-auto bg-transparent">
            <a href="/invitation/storage" className="pointer-auto">
              <p className="text-[#C1A56C]">{"<<"} 뒤로가기</p>
            </a>
          </button>
        ) : (
          " "
        )}

        {isExist && (
          <div className="absolute mt-8 right-20 flex space-x-4 z-30">
            <button
              className="bg-transparent text-[#C1A56C] px-2 py-2 rounded"
              onClick={handleEdit}
            >
              수정
            </button>
            <span className="text-[#C1A56C] py-2">|</span>
            <button
              className="bg-transparent text-[#C1A56C] px-2 py-2 rounded"
              onClick={handleDelete}
            >
              삭제
            </button>
          </div>
        )}

        <div className="flex justify-center items-center relative z-10 pointer-none">
          {showThumbnail ? (
            <img
              src={beige}
              alt="썸네일"
              className={`max-w-xs md:max-w-md lg:max-w-lg h-auto ${fadeClass}`}
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
                      {`${invitationData.groomFirstName} ఇ ${invitationData.brideFirstName}`}
                    </Fade>
                  </div>
                )}
                {showSecondFade && (
                  <div className="text-[20px] w-full flex justify-center">
                    <Fade direction={"up"} className="slide-up text-center">
                      {invitationData.date
                        .replace("년", ".")
                        .replace("월", ".")
                        .replace("일", "")
                        .slice(0, -3) + " "}
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
                {invitationData.groomLastName}
                {invitationData.groomFirstName}
                <span className="w-8 mx-10">
                  <img src={leaf} alt="장식 이미지" />
                </span>
                {invitationData.brideLastName}
                {invitationData.brideFirstName}
              </p>
            </div>

            <FallingSnow />

            <div className="flex flex-col" data-aos="fade-up">
              <div className="w-full flex justify-center mt-10 relative">
                <img
                  src={invitationData.url}
                  alt="대표 사진"
                  className="w-1/3 h-auto"
                />
              </div>
            </div>

            <div className="text-xl">
              <p className="mt-10">
                {invitationData.date}{" "}
                {invitationData.timezone === "AM" ? "오전" : "오후"}{" "}
                {invitationData.hour}시{" "}
                {invitationData.minute > 0 ? `${invitationData.minute}분` : ""}
              </p>
              <p className="mt-5">
                {invitationData.weddingHall}, {invitationData.addressDetail}
              </p>

              <div className="flex justify-center mt-20" data-aos="fade-up">
                <div className="w-[480px] min-h-[400px] bg-[#FDF5E6] mb-20">
                  <div className="flex justify-center">
                    <img
                      src={flower_sm}
                      alt="꽃 이미지"
                      className="w-10 mt-5 mb-5"
                    />
                  </div>
                  <p className="text-[#B3B3B3] letter-space-sm mb-5">
                    INVITATION
                  </p>
                  <div className="border border-gray-200 mx-10 mb-10"></div>
                  <p className="text-[16px] mx-24 mb-10">
                    {invitationData.greetings.split("\n").map((line, index) => (
                      <React.Fragment key={index}>
                        {line}
                        <br />
                      </React.Fragment>
                    ))}
                  </p>

                  <div className="border border-gray-200 mx-10 mb-10"></div>
                  <div className="text-[16px]">
                    <p>
                      <span className="font-semibold">
                        {invitationData.groomFatherLastName}
                        {invitationData.groomFatherFirstName}ㆍ
                        {invitationData.groomMotherLastName}
                        {invitationData.groomMotherFirstName}
                      </span>
                      <span className="text-[#A88E97]">
                        의 {groomBirthOrderLabel}{" "}
                      </span>
                      <span className="font-semibold">
                        {invitationData.groomFirstName}
                      </span>
                    </p>
                    <p className="mb-10">
                      <span className="font-semibold">
                        {invitationData.brideFatherLastName}
                        {invitationData.brideFatherFirstName}ㆍ
                        {invitationData.brideMotherLastName}
                        {invitationData.brideMotherFirstName}
                      </span>
                      <span className="text-[#A88E97]">
                        의 {brideBirthOrderLabel}{" "}
                      </span>
                      <span className="font-semibold">
                        {invitationData.brideFirstName}
                      </span>
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
                    <p className="text-[18px] mb-5">{invitationData.date}</p>
                    <div className="flex justify-center mb-20">
                      <Calendar
                        className="detailcalendar"
                        value={markDate}
                        locale="en-US"
                        formatDay={(_, date) => dayjs(date).format("DD")}
                        tileClassName={({ date }) =>
                          date.getFullYear() === markDate.getFullYear() &&
                          date.getMonth() === markDate.getMonth() &&
                          date.getDate() === markDate.getDate()
                            ? "highlight"
                            : null
                        }
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
                    <p className="text-[18px]">{invitationData.address}</p>
                    <p className="mb-10 text-[18px]">
                      {invitationData.weddingHall}{" "}
                      {invitationData.addressDetail}
                    </p>
                  </div>
                </div>
              </div>

              <div className="flex justify-center" data-aos="fade-up">
                <div className="mb-20 w-[400px]">
                  <InvitationMap
                    latitude={invitationData.latitude}
                    longitude={invitationData.longitude}
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

              <div
                className="flex flex-col items-center mt-5"
                data-aos="fade-up"
              >
                <div className="w-[400px] bg-[#F4F0EB] flex items-center justify-between p-2">
                  <p className="text-[18px] flex-1 text-center p-2">
                    축의금 계좌번호
                  </p>
                </div>

                <div className="w-[400px] flex bg-white p-2 mb-20 justify-between">
                  <div>
                    <p className="text-[15px] ml-5">
                      {invitationData.coupleBankName}{" "}
                      {invitationData.coupleAccount}
                    </p>
                    <p className="text-[13px]">
                      {"("}예금주: {invitationData.coupleAccountOwner}
                      {")"}
                    </p>
                  </div>
                  <button
                    className={`h-10 mr-5 text-sm mt-2 border border-gray-300 rounded-md shadow-sm px-2 py-1 ${
                      isButtonEnabled
                        ? "text-gray-800 hover:shadow-md transition-shadow"
                        : "text-gray-400 cursor-not-allowed"
                    }`}
                    onClick={isButtonEnabled ? shareDeepLink : () => {}}
                    disabled={!isButtonEnabled}
                  >
                    이체하기
                  </button>
                </div>
              </div>
            </div>
          </>
        ) : (
          ""
        )}

        {!showThumbnail ? (
          isExist ? (
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
          )
        ) : (
          " "
        )}
      </div>
    </div>
  );
};

export default StorageDetail;
