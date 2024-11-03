import api from "../utils/instance";

export const deleteFormalInvitation = async (invitationId: number): Promise<void> => {
  try {
    await api.delete(`/invitation/formal/${invitationId}`, {
      headers: {
        "Content-Type": "application/json",
      },
    });
  } catch (error) {
    console.error("정보형 청첩장 삭제 중 오류 발생:", error);
    throw error;
  }
};
