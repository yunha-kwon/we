import React, { useState, useRef, useEffect } from "react";
import Navbar from "../../Components/Navbar";
import ImageDropzone from "./ImageDropzone";
import HusbandInfo from "./HusbandInfo";
import BrideInfo from "./BrideInfo";
import GreetingsSection from "./Greetings";
import LocationAndDate from "./LocationAndDate";
import { inputImage } from "../../apis/api/imagedropzone";
import { useParams } from "react-router-dom";
import {
  getFormalInvitation,
  GetFormalInvitationDto,
} from "../../apis/api/getinfotypeinvitation";
import Swal from "sweetalert2";

interface HusbandInfoHandle {
  submit: () => void;
}

interface BrideInfoHandle {
  submit: () => void;
}

interface GreetingsHandle {
  submit: () => void;
}

interface LocationAndDateHandle {
  submit: () => void;
}

const InvitationEdit: React.FC = () => {
  const [selectedImage, setSelectedImage] = useState<File | null>(null);
  const [imageSrc, setImageSrc] = useState<string | null>(null);
  const [greetings, setGreetings] = useState<string>("");
  const [title, setTitle] = useState<string>("");

  const husbandInfoRef = useRef<HusbandInfoHandle | null>(null);
  const brideInfoRef = useRef<BrideInfoHandle | null>(null);
  const greetingsRef = useRef<GreetingsHandle | null>(null);
  const locationAndDateRef = useRef<LocationAndDateHandle | null>(null);

  const { invitationId } = useParams();

  useEffect(() => {
    const fetchInvitationData = async () => {
      if (invitationId) {
        try {
          const data: GetFormalInvitationDto = await getFormalInvitation(
            Number(invitationId)
          );

          setTitle(data.title || "");
          setImageSrc(data.url || null);
          setGreetings(data.greetings || "");
        } catch (error) {
          console.error("초대장 데이터를 가져오는 중 오류 발생:", error);
        }
      }
    };

    fetchInvitationData();
  }, [invitationId]);

  const handleImageChange = (file: File | null, imageSrc: string | null) => {
    setSelectedImage(file);
    setImageSrc(imageSrc);
  };

  const ImageUpload = async () => {
    if (invitationId) {
      const dto: any = {
        title: title,
      };
  
      if (selectedImage) {
        dto.url = selectedImage;
      }
  
      try {
        await inputImage(invitationId, dto);
      } catch (error) {
        console.error("이미지 업로드 중 오류 발생:", error);
      }
    }
  };
  

  const handleCreate = async () => {
    await ImageUpload();

    if (husbandInfoRef.current) {
      await husbandInfoRef.current.submit();
    } else {
      alert("신랑 정보를 입력해 주세요.");
    }

    if (brideInfoRef.current) {
      await brideInfoRef.current.submit();
    } else {
      alert("신부 정보를 입력해 주세요.");
    }

    if (greetingsRef.current) {
      await greetingsRef.current.submit();
    } else {
      alert("인사말을 작성해 주세요.");
    }

    if (locationAndDateRef.current) {
      await locationAndDateRef.current.submit();
    } else {
      alert("예식 장소와 일자를 입력해 주세요.");
    }
    await Swal.fire({
      text: "성공적으로 저장했습니다!",
      icon: "success",
      confirmButtonText: "확인",
      width: "400px",
      customClass: {
        popup: "my-popup-class",
      },
    });

    window.location.href = "/invitation/storage";
  };

  return (
    <div className="font-nanum">
      <Navbar isScrollSensitive={false} />
      <div className="mt-40 font-default flex flex-col items-center justify-center">
        <input
          id="title"
          name="title"
          type="text"
          placeholder="청첩장 제목을 입력하세요"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          className="w-full mb-20 text-center px-4 py-3 border-b text-md focus:outline-none focus:border-gray-700 bg-[#fcfaf5]"
          required
        />
        <ImageDropzone
          onImageChange={handleImageChange}
          initialImage={imageSrc || undefined}
        />
        <div className="w-full text-center">
          <p className="mt-20 text-md font-semibold">
            메인 사진을 선택해 주세요.
          </p>
          <div className="mt-20 border border-gray-200"></div>
        </div>
        <div>
          <HusbandInfo ref={husbandInfoRef} />
          <BrideInfo ref={brideInfoRef} />
          <GreetingsSection
            ref={greetingsRef}
            value={greetings}
            onChange={(e) => setGreetings(e.target.value)}
          />
        </div>
        <LocationAndDate ref={locationAndDateRef} />
        <div className="w-full flex justify-end gap-3 mt-10 mb-10">
          <button
            className="w-24 h-10 text-sm rounded-md text-md bg-[#FFD0DE]"
            onClick={handleCreate}
          >
            저장하기
          </button>
        </div>
      </div>
    </div>
  );
};

export default InvitationEdit;