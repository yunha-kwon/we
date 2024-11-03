import React, { useState } from "react";
import AOS from "aos";
import "aos/dist/aos.css";
import video_intro from "../assets/images/video_intro.mp4";
import happymoney from "../assets/images/happymoney.png";
import invitation from "../assets/images/invitation.png";
import calendar from "../assets/images/calendar.png";
import Navbar from "../Components/Navbar";
import Modal from "../Components/Modal";

const Home: React.FC = () => {
  const [showModal, setShowModal] = useState(false);
  const [modalMessage, setModalMessage] = useState("");
  const [, setRedirectUrl] = useState("");

  React.useEffect(() => {
    AOS.init({ duration: 2000 });
  }, []);

  const isLoggedIn = !!localStorage.getItem("accessToken");

  const handleImageClick = (url: string, message: string) => {
    if (!isLoggedIn) {
      setModalMessage(message);
      setRedirectUrl(url);
      setShowModal(true);
    } else {
      window.location.href = url;
    }
  };

  const closeModal = () => setShowModal(false);

  const redirectToLogin = () => {
    closeModal();
    window.location.href = "/login";
  };

  const handleCalendarClick = () => {
    if (!isLoggedIn) {
      setModalMessage("해당 서비스는 앱에서만 지원됩니다.");
      setShowModal(true);
    } else {
      setModalMessage("해당 서비스는 앱에서만 지원됩니다.");
      setShowModal(true);
    }
  };

  return (
    <div className="font-nanum box-border bg-[#FFFFFF]">
      <Navbar isScrollSensitive={true} />

      <div className="relative w-full overflow-hidden">
        <video
          muted
          autoPlay
          loop
          className="opacity-70 w-full h-auto object-cover"
        >
          <source src={video_intro} type="video/mp4" />
        </video>
      </div>

      <div className="flex flex-col mt-60 items-center text-center">
        <h2 className="text-3xl sm:text-4xl md:text-5xl mb-8">[ WE : ]</h2>
        <div className="mb-60 text-sm sm:text-base md:text-lg">
          <p>결혼이라는 새로운 출발을 앞둔 '우리(WE)'와,</p>
          <p>함께 준비하는 '웨딩(WEdding)'의 의미를 지녔습니다.</p>
          <p>결혼 자금과 축의금 장부를 효과적으로 관리하고,</p>
          <p>
            간편한 결혼 준비 체크리스트 확인과 직접 만드는 모바일 청첩장까지,
          </p>
          <p>결혼 준비의 시작부터 끝까지 [ WE : ]는 여러분과 함께 합니다.</p>
        </div>
      </div>

      <div className="mb-60 mx-10 border border-gray-100"></div>
      <div className="items-center text-center text-2xl sm:text-3xl md:text-4xl mb-20">
        Services
      </div>

      <div className="flex justify-around gap-10 mt-20 mb-10 mx-10">
        <div
          className="flex flex-col"
          data-aos="fade-up"
          onClick={() =>
            handleImageClick("/account", "로그인 후 이용 가능한 서비스입니다.")
          }
        >
          <img
            src={happymoney}
            alt="QR 인식 송금"
            className="mb-3 h-48 sm:h-60 md:h-80 w-auto cursor-pointer"
          />
          <div className="mx-2">
            <p className="text-sm sm:text-lg text-gray-400">Ep 01</p>
            <p className="text-lg sm:text-xl font-bold mb-4">축의금 관리</p>
            <p className="text-md">
              QR 인식으로 보다 편리한 축의금 송금과 장부 관리
            </p>
          </div>
        </div>
        <div
          className="flex flex-col"
          data-aos="fade-up"
          onClick={() =>
            handleImageClick(
              "/invitation",
              "로그인 후 이용 가능한 서비스입니다."
            )
          }
        >
          <img
            src={invitation}
            alt="모바일 청첩장"
            className="mb-3 h-48 sm:h-60 md:h-80 w-auto cursor-pointer"
          />
          <div className="mx-2">
            <p className="text-sm sm:text-lg text-gray-400">Ep 02</p>
            <p className="text-lg sm:text-xl font-bold mb-4">
              모바일 청첩장 제작
            </p>
            <p className="text-md">
              필요한 정보만 입력한 만들어지는 정보형 모바일 청첩장
            </p>
          </div>
        </div>
        <div
          className="flex flex-col"
          data-aos="fade-up"
          onClick={handleCalendarClick}
        >
          <img
            src={calendar}
            alt="결혼준비 체크리스트"
            className="mb-3 h-48 sm:h-60 md:h-80 w-auto cursor-pointer"
          />
          <div className="mx-2">
            <p className="text-sm sm:text-lg text-gray-400">Ep 03</p>
            <p className="text-lg sm:text-xl font-bold mb-4">일정 관리</p>
            <p className="text-md">
              복잡한 결혼준비 체크리스트, 캘린더로 손쉬운 일정 관리
            </p>
          </div>
        </div>
      </div>

      <footer className="mt-20 text-xs sm:text-sm md:text-base text-gray-600 py-4 text-center bg-gray-100">
        <p>ⓒ WE 웨딩</p>
        <p>삼성 청년 SW 아카데미 D104팀</p>
        <p>Email: wewedding@ssafy.com</p>
      </footer>

      {showModal && (
        <Modal
          message={modalMessage}
          onClose={closeModal}
          onRedirect={redirectToLogin}
          showRedirectButton={
            modalMessage === "로그인 후 이용 가능한 서비스입니다."
          }
          redirectButtonText="로그인하기"
        />
      )}
    </div>
  );
};

export default Home;
