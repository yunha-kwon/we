import { useState, forwardRef, useImperativeHandle, useEffect } from "react";
import {
  GroomInfoDto,
  BirthOrder,
  inputGroomInfo,
} from "../../apis/api/groominfo";
import { useParams } from "react-router-dom";
import { getFormalInvitation } from "../../apis/api/getinfotypeinvitation";

const HusbandInfo = forwardRef((_, ref) => {
  const [lastName, setGroomLastName] = useState("");
  const [firstName, setGroomFirstName] = useState("");
  const [birthOrder, setGroomBirthOrder] = useState<BirthOrder | "">("");
  const [fatherLastName, setGroomFatherLastName] = useState("");
  const [fatherFirstName, setGroomFatherFirstName] = useState("");
  const [motherLastName, setGroomMotherLastName] = useState("");
  const [motherFirstName, setGroomMotherFirstName] = useState("");
  const { invitationId } = useParams();

  useEffect(() => {
    const fetchData = async () => {
      if (invitationId) {
        try {
          const response = await getFormalInvitation(Number(invitationId));
          setGroomLastName(response.groomLastName || "");
          setGroomFirstName(response.groomFirstName || "");
          setGroomBirthOrder(response.groomBirthOrder || "");
          setGroomFatherLastName(response.groomFatherLastName || "");
          setGroomFatherFirstName(response.groomFatherFirstName || "");
          setGroomMotherLastName(response.groomMotherLastName || "");
          setGroomMotherFirstName(response.groomMotherFirstName || "");
        } catch (error) {
          console.error("기존 신랑 정보 불러오기 중 오류 발생:", error);
        }
      }
    };

    fetchData();
  }, [invitationId]);

  const handleSubmit = async () => {
    if (!birthOrder) {
      return;
    }

    const dto: GroomInfoDto = {
      lastName,
      firstName,
      birthOrder: birthOrder as BirthOrder,
      fatherLastName,
      fatherFirstName,
      motherLastName,
      motherFirstName,
    };

    try {
      if (dto && invitationId) {
        await inputGroomInfo(invitationId, dto);
      }
    } catch (error) {
      console.error("신랑 정보 업로드 중 오류 발생:", error);
      alert("신랑 정보 업로드 중 오류가 발생했습니다.");
    }
  };

  useImperativeHandle(ref, () => ({
    submit: handleSubmit,
  }));

  return (
    <div>
      <p className="mt-20 mb-5 text-md text-center font-semibold">
        신랑 측 정보를 작성해 주세요.
      </p>
      <div className="flex justify-between gap-5 mb-3">
        <input
          id="groom-last-name"
          name="groom-last-name"
          type="text"
          placeholder="신랑 성"
          value={lastName}
          onChange={(e) => setGroomLastName(e.target.value)}
          className="px-2 py-2 border text-md border-gray-400 focus:border-gray-400 text-center bg-white"
          required
        />
        <input
          id="groom-name"
          name="groom-name"
          type="text"
          placeholder="신랑 이름"
          value={firstName}
          onChange={(e) => setGroomFirstName(e.target.value)}
          className="px-2 py-2 border text-md border-gray-400 focus:border-gray-400 text-center bg-white"
          required
        />
      </div>

      <div className="flex mb-3">
        <select
          id="groom-order"
          name="groom-order"
          value={birthOrder}
          onChange={(e) => setGroomBirthOrder(e.target.value as BirthOrder)}
          className={`w-full px-2 py-2 border text-md border-gray-400 focus:border-gray-400 text-center bg-white ${
            birthOrder === "" ? "text-gray-400" : "text-black"
          }`}
        >
          <option value="" disabled hidden>
            신랑 서열 (장남 / 차남 / 아들)
          </option>
          <option value="FIRST">장남</option>
          <option value="SECOND">차남</option>
          <option value="OTHER">아들</option>
        </select>
      </div>

      <div className="flex justify-between gap-5 mb-3">
        <input
          id="groom-father-last-name"
          name="groom-father-last-name"
          type="text"
          placeholder="신랑 아버님 성"
          value={fatherLastName}
          onChange={(e) => setGroomFatherLastName(e.target.value)}
          className="px-2 py-2 border text-md border-gray-400 focus:border-gray-400 text-center bg-white"
          required
        />
        <input
          id="groom-father-name"
          name="groom-father-name"
          type="text"
          placeholder="신랑 아버님 이름"
          value={fatherFirstName}
          onChange={(e) => setGroomFatherFirstName(e.target.value)}
          className="px-2 py-2 border text-md border-gray-400 focus:border-gray-400 text-center bg-white"
          required
        />
      </div>

      <div className="flex justify-between gap-5 mb-3">
        <input
          id="groom-mother-last-name"
          name="groom-mother-last-name"
          type="text"
          placeholder="신랑 어머님 성"
          value={motherLastName}
          onChange={(e) => setGroomMotherLastName(e.target.value)}
          className="px-2 py-2 border text-md border-gray-400 focus:border-gray-400 text-center bg-white"
          required
        />
        <input
          id="groom-mother-name"
          name="groom-mother-name"
          type="text"
          placeholder="신랑 어머님 이름"
          value={motherFirstName}
          onChange={(e) => setGroomMotherFirstName(e.target.value)}
          className="px-2 py-2 border text-md border-gray-400 focus:border-gray-400 text-center bg-white"
          required
        />
      </div>
    </div>
  );
});

export default HusbandInfo;
