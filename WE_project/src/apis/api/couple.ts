import api from "../utils/instance";

export type CoupleInfoMember = {
  id: number;
  email: string;
  nickname: string;
  imageUrl: string | null;
  regDate: string; 
  leavedDate: string | null;
  coupleJoined: boolean;
  leaved: boolean;
};

export type CoupleInfoData = {
  coupleInfo: {
    id: number;
    member1: CoupleInfoMember;
    member2: CoupleInfoMember;
    bankbookCreated: boolean;
    accountNumber: string;
    ledgerCreated: boolean;
  };
};

export type GetCoupleInfoDto = {
  message: string;
  data: CoupleInfoData;
};

export const getCoupleInfo = async (
    accessToken: string
  ): Promise<GetCoupleInfoDto> => {
    try {
      const response = await api.get("/couples", {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${accessToken}`,
        },
      });
      return response.data;
    } catch (error) {
      console.error("커플 청첩장 조회 중 오류 발생:", error);
      throw error;
    }
  };