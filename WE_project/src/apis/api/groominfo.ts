import api from "../utils/instance";

export enum BirthOrder {
  FIRST = "FIRST", //장남
  SECOND = "SECOND", //차남
  OTHER = "OTHER", //아들
}

export type GroomInfoDto = {
  lastName: string;
  firstName: string;
  birthOrder: BirthOrder;

  fatherLastName: string;
  fatherFirstName: string;
  motherLastName: string;
  motherFirstName: string;
};

export const inputGroomInfo = async (
  invitationId: string,
  dto: GroomInfoDto
): Promise<void> => {
  try {
    const response = await api.patch(
      `/invitation/formal/groom/${invitationId}`,
      dto,
      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );

    return response.data;
  } catch (error) {
    console.error("신랑 정보 업로드 중 오류 발생:", error);
    throw error;
  }
};
