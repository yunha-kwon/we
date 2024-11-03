import api from "../utils/instance";

export type LogoutResponse = {
  message: string;
};

export const logout = async (accessToken: string): Promise<LogoutResponse> => {
  try {
    const response = await api.post<LogoutResponse>(
      `/auth/logout`,
      {},
      {
        headers: {
          Authorization: `Bearer ${accessToken}`,
          "Content-Type": "application/json",
        },
      }
    );
    return response.data;
  } catch (error) {
    console.error("Error during logout:", error);
    throw error;
  }
};
