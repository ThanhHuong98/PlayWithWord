package com.hfda.playwithwords;
//mục đích chính của cái interface này là Activity sẽ đọc dữ liệu từ database rồi gửi xuống Fragment bên dưới
public interface fromContainerToFrag
{
    void InfoToHandle(String mess, String roundOfMode, Object question, Object answer, String transcription, String[] answerInBtn);
}
