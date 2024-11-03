import api from "../utils/instance";

export type JoinMemberInfoDto = {
  nickname: string;
  email: string;
  password: string;
};

export const joinMember = async (dto: JoinMemberInfoDto): Promise<void> => {
  try {
    const response = await api.post(`/members/register`, dto, {
      headers: {
        "Content-Type": "application/json",
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error during member registration:", error);
    throw error;
  }
};
