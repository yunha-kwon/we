import React, { useState, useRef, useEffect } from "react";
import Cropper from "cropperjs";
import "cropperjs/dist/cropper.min.css";
import dropzone from "../../assets/images/dropzone.jpg";

interface ImageDropzoneProps {
  onImageChange: (file: File | null, imageSrc: string | null) => void;
  initialImage?: string | null;
}

const ImageDropzone: React.FC<ImageDropzoneProps> = ({ onImageChange, initialImage = null }) => {
  const [imageSrc, setImageSrc] = useState<string | null>(initialImage);
  const [selectedImage, setSelectedImage] = useState<File | null>(null);
  const [showCropper, setShowCropper] = useState(false);
  const cropperRef = useRef<HTMLImageElement>(null);
  const [cropper, setCropper] = useState<Cropper | null>(null);
  const [croppedImageSrc, setCroppedImageSrc] = useState<string | null>(null);

  useEffect(() => {
    if (cropperRef.current && imageSrc) {
      const newCropper = new Cropper(cropperRef.current, {
        viewMode: 1,
        autoCropArea: 1,
        responsive: true,
        background: false,
        cropBoxResizable: true,
        cropBoxMovable: true,
        movable: true,
        zoomable: true,
        scalable: true,
        rotatable: true,
      });
      setCropper(newCropper);
      return () => {
        newCropper.destroy();
      };
    }
  }, [imageSrc]);

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      const file = e.target.files[0];

      if (!file.type.startsWith("image/")) {
        alert("이미지 파일만 업로드 가능합니다.");
        return;
      }

      setImageSrc(URL.createObjectURL(file));
      setSelectedImage(file);
      setShowCropper(true);
    }
  };

  const handleCrop = () => {
    if (cropper) {
      const canvas = cropper.getCroppedCanvas();
      const croppedImageURL = canvas.toDataURL();
      setCroppedImageSrc(croppedImageURL);
      onImageChange(selectedImage, croppedImageURL);
      setShowCropper(false);
    }
  };

  const handleRemoveImage = () => {
    setSelectedImage(null);
    setImageSrc(null);
    setCroppedImageSrc(null);
    onImageChange(null, null);
  };

  return (
    <div className="relative w-80 h-80 items-center justify-center">
      {croppedImageSrc ? (
        <div className="relative flex items-center justify-center w-full h-full">
          <img
            src={croppedImageSrc || ""}
            alt="Cropped"
            className="max-w-full max-h-full min-w-[300px] min-h-[300px] object-contain"
          />
          <button
            onClick={handleRemoveImage}
            className="absolute top-2 right-2 bg-white text-gray-600 hover:bg-red-100 rounded-full p-1.5 shadow-md transition-all duration-300 ease-in-out"
          >
            <svg
              className="w-5 h-5"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth="2"
                d="M6 18L18 6M6 6l12 12"
              />
            </svg>
          </button>
        </div>
      ) : (
        <label
          className="flex flex-col items-center justify-center cursor-pointer w-full h-full"
          htmlFor="url"
        >
          <input
            id="url"
            type="file"
            accept="image/*"
            onChange={handleFileChange}
            className="hidden"
          />
          {initialImage ? (
            <img
              src={initialImage}
              alt="Existing"
              className="w-full h-full object-cover"
            />
          ) : (
            <img
              src={dropzone}
              alt="dropzone"
              className="w-full h-full object-cover"
            />
          )}
        </label>
      )}

      {showCropper && (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
          <div className="bg-white p-4 rounded-lg w-80 max-w-lg relative">
            <div className="mb-4">
              <img
                ref={cropperRef}
                src={imageSrc || ""}
                alt="Cropper"
                style={{ width: "100%", height: "auto" }}
              />
            </div>
            <div className="flex justify-end">
              <button
                onClick={() => setShowCropper(false)}
                className="px-4 py-2 bg-[#FFECCA] rounded-lg mr-2"
              >
                취소
              </button>
              <button
                onClick={handleCrop}
                className="px-4 py-2 bg-[#FFD0DE] rounded-lg"
              >
                완료
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default ImageDropzone;
