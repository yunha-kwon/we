import React, { useEffect, useState } from "react";
import { logout } from "../apis/api/logout";
import Modal from "./Modal";

interface NavbarProps {
  isScrollSensitive?: boolean;
}

const Navbar: React.FC<NavbarProps> = ({ isScrollSensitive = false }) => {
  const [navbarBackground, setNavbarBackground] = useState(false);
  const [dropdownVisible, setDropdownVisible] = useState(false);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [showModal, setShowModal] = useState(false);

  useEffect(() => {
    const accessToken = localStorage.getItem("accessToken");
    setIsLoggedIn(!!accessToken);

    if (isScrollSensitive) {
      const handleScroll = () => {
        if (window.scrollY > 50) {
          setNavbarBackground(true);
        } else {
          setNavbarBackground(false);
        }
      };

      window.addEventListener("scroll", handleScroll);

      return () => {
        window.removeEventListener("scroll", handleScroll);
      };
    }
  }, [isScrollSensitive]);
  

  const handleMouseEnter = () => setDropdownVisible(true);
  const handleMouseLeave = () => setDropdownVisible(false);

  const handleLogout = async () => {
    const accessToken = localStorage.getItem("accessToken");

    if (accessToken) {
      try {
        await logout(accessToken);
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
        setIsLoggedIn(false);
        window.location.href = "/";
      } catch (error) {
        console.error("로그아웃 실패:", error);
      }
    }
  };

  const handleLinkClick = (event: React.MouseEvent<HTMLAnchorElement>) => {
    if (!isLoggedIn) {
      event.preventDefault();
      setShowModal(true);
    }
  };

  const closeModal = () => setShowModal(false);

  const redirectToLogin = () => {
    closeModal();
    window.location.href = "/login";
  };

  return (
    <nav
      className={`fixed top-0 left-0 w-full flex items-center py-6 px-3 z-50 transition-colors duration-300 ease-in-out border-b ${
        navbarBackground || !isScrollSensitive
          ? "bg-white border-gray-300"
          : "bg-transparent border-white/30"
      }`}
    >
      <div
        className="mr-24 ml-12 text-base md:text-lg lg:text-2xl whitespace-nowrap"
        style={{ flexShrink: 0 }}
      >
        <a
          href="/"
          className={`${
            navbarBackground || !isScrollSensitive ? "text-black" : "text-white"
          }`}
        >
          [ WE : ]
        </a>
      </div>
      <div className="flex gap-8 md:gap-10 lg:gap-12 justify-center flex-grow">
        <div
          className="relative"
          onMouseEnter={handleMouseEnter}
          onMouseLeave={handleMouseLeave}
        >
          <a
            href="/invitation"
            onClick={handleLinkClick}
            className={`${
              navbarBackground || !isScrollSensitive
                ? "text-black"
                : "text-white"
            } text-sm md:text-base lg:text-lg`}
          >
            Mobile Invitation Card
          </a>
          {dropdownVisible && (
            <div className="absolute top-full w-48 bg-white shadow-lg border">
              <a
                href="/invitation"
                onClick={handleLinkClick}
                className="block px-4 py-2 hover:bg-black hover:text-white text-sm md:text-base lg:text-lg"
              >
                청첩장 만들기
              </a>
              <a
                href="/invitation/storage"
                onClick={handleLinkClick}
                className="block px-4 py-2 hover:bg-black hover:text-white text-sm md:text-base lg:text-lg"
              >
                내 청첩장 보관함
              </a>
            </div>
          )}
        </div>
        <a
          href="/account"
          onClick={handleLinkClick}
          className={`${
            navbarBackground || !isScrollSensitive ? "text-black" : "text-white"
          } text-sm md:text-base lg:text-lg`}
        >
          Account Book
        </a>
        <div className="flex gap-8 md:gap-10 lg:gap-12 ml-auto mr-12">
          {isLoggedIn ? (
            <>
              <a
                href="#"
                onClick={handleLogout}
                className={`cursor-pointer ${
                  navbarBackground || !isScrollSensitive
                    ? "text-black"
                    : "text-white"
                } text-sm md:text-base lg:text-lg`}
              >
                Logout
              </a>
            </>
          ) : (
            <>
              <a
                href="/signup"
                className={`${
                  navbarBackground || !isScrollSensitive
                    ? "text-black"
                    : "text-white"
                } text-sm md:text-base lg:text-lg`}
              >
                Sign Up
              </a>
              <a
                href="/login"
                className={`${
                  navbarBackground || !isScrollSensitive
                    ? "text-black"
                    : "text-white"
                } text-sm md:text-base lg:text-lg`}
              >
                Login
              </a>
            </>
          )}
        </div>
      </div>

      {showModal && (
        <Modal
          message="로그인 후 이용 가능한 서비스입니다."
          onClose={closeModal}
          onRedirect={redirectToLogin}
          showRedirectButton={true}
          redirectButtonText="로그인하기"
        />
      )}
    </nav>
  );
};

export default Navbar;
