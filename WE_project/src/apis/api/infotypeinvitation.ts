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

export type FormalInvitationDto = {
  url?: string;

  groomLastName?: string;
  groomFirstName?: string;
  groomBirthOrder?: BirthOrder;

  groomFatherLastName?: string;
  groomFatherFirstName?: string;
  groomMotherLastName?: string;
  groomMotherFirstName?: string;

  brideLastName?: string;
  brideFirstName?: string;
  brideBirthOrder?: BirthOrder;

  brideFatherLastName?: string;
  brideFatherFirstName?: string;
  brideMotherLastName?: string;
  brideMotherFirstName?: string;

  greetings?: string; // 인사말

  date?: string; // 예식일자
  timezone?: Timezone; // 오전 or 오후
  hour?: number; // 시
  minute?: number; // 분

  address?: string; // 주소
  addressDetail?: string; // 층과 홀
  weddingHall?: string; // 예식장 명
};

export const createFormalInvitation = async (
  accessToken: string,
  dto: FormalInvitationDto
): Promise<string> => {
  try {
    const response = await api.post(`/invitation/formal`, dto, {
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${accessToken}`,
      },
    });
    return response.data.invitation_id;
  } catch (error) {
    console.error("Formal invitation 생성 중 오류 발생:", error);
    throw error;
  }
};
