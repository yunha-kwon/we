import api from "../utils/instance";

export enum Timezone {
  AM = "AM", // 오전
  PM = "PM", // 오후
}

export type DateLocationDto = {
  date: string; // 예식일자
  timezone: Timezone; // 오전 오후
  hour: number; // 시
  minute: number; // 분

  address: string; // 주소
  address_detail: string; // 층과 홀
  wedding_hall: string; // 예식장 명
  latitude: number; // 위도
  longitude: number; // 경도
};

export const inputDateLocation = async (
  invitationId: string,
  dto: DateLocationDto
): Promise<void> => {
  try {
    const response = await api.patch(
      `/invitation/formal/meta-info/${invitationId}`,
      dto,
      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );

    return response.data;
  } catch (error) {
    console.error("일시/장소 정보 업로드 중 오류 발생:", error);
    throw error;
  }
};
