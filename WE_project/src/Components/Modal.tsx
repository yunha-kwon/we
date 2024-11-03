import React from "react";

interface ModalProps {
  message: string;
  onClose: () => void;
  onRedirect?: () => void;
  showRedirectButton?: boolean;
  redirectButtonText?: string;
}

const LoginAlertModal: React.FC<ModalProps> = ({
  message,
  onClose,
  onRedirect,
  showRedirectButton = true,
  redirectButtonText = "",
}) => {
  return (
    <div className="font-default fixed inset-0 flex items-center justify-center bg-gray-700 bg-opacity-50 z-50">
      <div className="bg-white p-6 rounded-md shadow-lg w-80 h-auto relative">
        <button
          onClick={onClose}
          className="absolute top-4 right-4 bg-transparent text-gray-500 hover:text-gray-900"
          aria-label="Close"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            className="h-5 w-5"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth={2}
              d="M6 18L18 6M6 6l12 12"
            />
          </svg>
        </button>
        <p className="text-sm font-bold mb-6 mt-6 ml-2">{message}</p>
        {showRedirectButton && (
          <div className="flex justify-end mr-6">
            <button
              onClick={onRedirect}
              className="px-4 py-2 bg-[#FFD0DE] text-sm rounded-md"
            >
              {redirectButtonText}
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

export default LoginAlertModal;
