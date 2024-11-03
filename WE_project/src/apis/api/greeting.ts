import api from "../utils/instance";

export type GreetingDto = {
  greetings: string;
};

export const inputGreeting = async (
  invitationId: string,
  dto: GreetingDto
): Promise<void> => {
  try {
    const response = await api.patch(
      `/invitation/formal/greetings/${invitationId}`,
      dto,
      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );

    return response.data;
  } catch (error) {
    console.error("인사말 업로드 중 오류 발생:", error);
    throw error;
  }
};
