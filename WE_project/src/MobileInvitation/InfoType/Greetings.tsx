import React, {
  useState,
  forwardRef,
  useImperativeHandle,
  useEffect,
} from "react";
import { inputGreeting } from "../../apis/api/greeting";
import { useParams } from "react-router-dom";

interface GreetingsProps {
  value?: string;
  onChange: (event: React.ChangeEvent<HTMLTextAreaElement>) => void;
}

interface GreetingsHandle {
  submit: () => void;
}

const GreetingsSection = forwardRef<GreetingsHandle, GreetingsProps>(
  ({ value, onChange }, ref) => {
    const { invitationId } = useParams();
    const [greeting, setGreeting] = useState("");

    useEffect(() => {
      if (value) {
        setGreeting(value);
      }
    }, [value]);

    const handleSubmit = async () => {
      try {
        if (invitationId) {
          await inputGreeting(invitationId, { greetings: greeting });
        }
      } catch (error) {
        console.error("인사말 업로드 중 오류 발생:", error);
        alert("인사말 업로드 중 오류가 발생했습니다.");
      }
    };

    useImperativeHandle(ref, () => ({
      submit: handleSubmit,
    }));

    return (
      <div className="w-full mt-20 text-center">
        <p className="font-semibold">인사말을 작성해 주세요.</p>
        <textarea
          id="greetings"
          name="greetings"
          placeholder="인사말 작성"
          value={greeting}
          onChange={(e) => {
            setGreeting(e.target.value);
            onChange(e);
          }}
          className="mt-5 custom-textarea px-4 py-2 border border-gray-400 focus:border-gray-400 bg-white text-gray-700"
          required
        />
        <div className="mt-20 border border-gray-200"></div>
      </div>
    );
  }
);

export default GreetingsSection;
