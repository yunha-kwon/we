import api from "../utils/instance";

export type GetAccountInfoDto = {
  data: {
    accountNo: number;
    bankName: string;
  };
};

export const getAccountInfo = async (
  accessToken: string
): Promise<GetAccountInfoDto> => {
  try {
    const response = await api.get("/bank/my-couple-account", {
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${accessToken}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error("은행 계좌 조회 중 오류 발생:", error);
    throw error;
  }
};
