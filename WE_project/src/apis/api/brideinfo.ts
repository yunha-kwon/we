import api from "../utils/instance";

export enum BirthOrder {
  FIRST = "FIRST", //장녀
  SECOND = "SECOND", //차녀
  OTHER = "OTHER", //딸
}

export type BrideInfoDto = {
  lastName: string;
  firstName: string;
  birthOrder: BirthOrder;

  fatherLastName: string;
  fatherFirstName: string;
  motherLastName: string;
  motherFirstName: string;
};

export const inputBrideInfo = async (
  invitationId: string,
  dto: BrideInfoDto
): Promise<void> => {
  try {
    const response = await api.patch(
      `/invitation/formal/bride/${invitationId}`,
      dto,
      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );

    return response.data;
  } catch (error) {
    console.error("신부 정보 업로드 중 오류 발생:", error);
    throw error;
  }
};
