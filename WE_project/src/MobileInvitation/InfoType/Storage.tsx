import React, { useEffect, useState, useRef } from "react";
import Navbar from "../../Components/Navbar";
import {
  GetCoupleInvitationDto,
  getCoupleInvitation,
} from "../../apis/api/coupleinvitation";
import { ChevronLeft, ChevronRight } from "react-feather";

const Storage: React.FC = () => {
  const [invitations, setInvitations] = useState<GetCoupleInvitationDto[]>([]);
  const [currentIndex, setCurrentIndex] = useState(0);
  const accessToken = localStorage.getItem("accessToken");
  const carouselRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const fetchCoupleInvitations = async () => {
      try {
        if (!accessToken) {
          throw new Error("Access token not found");
        }

        const response = await getCoupleInvitation(accessToken);

        const rearrangedInvitations = rearrangeArrayToCenterLatest(response);

        setInvitations(rearrangedInvitations);

        setCurrentIndex(Math.floor(rearrangedInvitations.length / 2));
      } catch (err) {
        console.error(err);
      }
    };

    fetchCoupleInvitations();
  }, [accessToken]);

  const rearrangeArrayToCenterLatest = (array: GetCoupleInvitationDto[]) => {
    const length = array.length;
    if (length <= 1) return array;

    const middleIndex = Math.floor(length / 2);
    const latestInvitation = array[length - 1];

    const remainingInvitations = array.slice(0, length - 1);
    remainingInvitations.splice(middleIndex, 0, latestInvitation);

    return remainingInvitations;
  };

  const handleNext = () => {
    setCurrentIndex((prevIndex) => (prevIndex + 1) % invitations.length);
  };

  const handlePrev = () => {
    setCurrentIndex(
      (prevIndex) => (prevIndex - 1 + invitations.length) % invitations.length
    );
  };

  return (
    <div className="font-nanum min-w-[1260px]">
      <Navbar isScrollSensitive={false} />
      <div className="mt-20 flex flex-col items-center relative">
        {invitations.length > 0 ? (
          <div className="relative w-full max-w-md">
            <div
              ref={carouselRef}
              className="flex transition-transform duration-500"
              style={{ transform: `translateX(-${currentIndex * 100}%)` }}
            >
              {invitations.map((invitation, index) => (
                <div
                  key={invitation.invitationId}
                  className={`flex-none w-full flex flex-col items-center mb-10 transition-transform duration-500 ${
                    index === currentIndex
                      ? "scale-110 z-10"
                      : "scale-90 opacity-60"
                  }`}
                >
                  <a
                    href={`/invitation/storage/${invitation.invitationId}`}
                    className="transform transition-transform duration-300 hover:scale-105"
                  >
                    <img
                      src={invitation.url}
                      alt="thumbnail"
                      className="w-72 h-96 object-cover shadow-lg rounded-lg"
                    />
                    <p className="text-center mt-5">
                      {invitation.title
                        ? invitation.title
                        : `청첩장 ${invitation.invitationId}`}
                    </p>
                  </a>
                </div>
              ))}
            </div>
            <button
              className="absolute left-0 p-2 bg-transparent"
              onClick={handlePrev}
            >
              <ChevronLeft />
            </button>
            <button
              className="absolute right-0 p-2 bg-transparent"
              onClick={handleNext}
            >
              <ChevronRight />
            </button>
            <div className="flex justify-center mt-4">
              {invitations.map((_, index) => (
                <span
                  key={index}
                  className={`mx-1 h-2 w-2 rounded-full ${
                    index === currentIndex ? "bg-black" : "bg-gray-300"
                  }`}
                ></span>
              ))}
            </div>
          </div>
        ) : (
          ""
        )}
      </div>
    </div>
  );
};

export default Storage;
