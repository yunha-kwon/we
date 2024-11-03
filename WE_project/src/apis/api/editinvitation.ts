import api from "../utils/instance";

export enum BirthOrder {
  FIRST = "FIRST", //장남 or 장녀
  SECOND = "SECOND", //차남 or 차녀
  OTHER = "OTHER", //아들 or 딸
}

export enum Timezone {
  AM = "AM", //오전
  PM = "PM", //오후
}

export type EditInvitationDto = {
  url: string;
  title: string;

  groomLastName: string;
  groomFirstName: string;
  groomBirthOrder: BirthOrder;

  groomFatherLastName: string;
  groomFatherFirstName: string;
  groomMotherLastName: string;
  groomMotherFirstName: string;

  brideLastName: string;
  brideFirstName: string;
  brideBirthOrder: BirthOrder;

  brideFatherLastName: string;
  brideFatherFirstName: string;
  brideMotherLastName: string;
  brideMotherFirstName: string;

  greetings: string;

  date: string;
  timezone: Timezone;
  hour: number;
  minute: number;

  address: string;
  addressDetail: string;
  weddingHall: string;

  longitude: number;
  latitude: number;
};

export const editInvitation = async (
  invitationId: number
): Promise<EditInvitationDto[]> => {
  try {
    const response = await api.patch(`/invitation/formal/${invitationId}`, {
      headers: {
        "Content-Type": "application/json",
      },
    });
    return response.data;
  } catch (error) {
    console.error("커플 청첩장 조회 중 오류 발생:", error);
    throw error;
  }
};
