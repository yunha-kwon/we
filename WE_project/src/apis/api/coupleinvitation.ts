import api from "../utils/instance";

export type GetCoupleInvitationDto = {
  invitationId: number;
  coupleId: number;
  title: string;
  url: string;
};

export const getCoupleInvitation = async (
  accessToken: string
): Promise<GetCoupleInvitationDto[]> => {
  try {
    const response = await api.get("/invitation/formal/couple", {
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
