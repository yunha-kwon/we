import api from "../utils/instance";

export type MemberInfo = {
  id: number;
  email: string;
  nickname: string;
  imageUrl: string | null;
  regDate: string;
  leavedDate: string | null;
  coupleJoined: boolean;
  leaved: boolean;
};

export type AccountBookData = {
  id: number;
  memberInfo: MemberInfo;
  isBride: boolean | null;
  charge: number;
  message: string | null;
};

export type GetAccountBook = {
  message: string;
  data: AccountBookData[];
};

export const getAccountBook = async (
  accessToken: string
): Promise<GetAccountBook> => {
  try {
    const response = await api.get("/ledgers/myGift", {
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${accessToken}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error("장부 조회 중 오류 발생:", error);
    throw error;
  }
};
