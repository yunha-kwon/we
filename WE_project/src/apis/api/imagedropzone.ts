import api from "../utils/instance";

export type ImageDto = {
  url: File;
  title: string;
};

export const inputImage = async (
  invitationId: string,
  dto: ImageDto
): Promise<void> => {
  try {
    const formData = new FormData();
    formData.append("file", dto.url);
    formData.append("title", dto.title);

    const response = await api.post(
      `/invitation/formal/file/${invitationId}`,
      formData,
      {}
    );

    return response.data;
  } catch (error) {
    console.error("이미지 업로드 중 오류 발생:", error);
    throw error;
  }
};
