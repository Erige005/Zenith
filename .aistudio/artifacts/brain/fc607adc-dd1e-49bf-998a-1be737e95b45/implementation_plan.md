# Kế hoạch phát triển: Zenith - Minimalist Brain & Puzzle Arcade

## 1. Giới thiệu dự án
Dựa trên mong muốn của bạn về một ứng dụng **Game giải trí & sáng tạo** với phong cách **Tối giản (Minimalist), Hiện đại và Thanh lịch**, dự án **Zenith** sẽ mang lại trải nghiệm thư giãn nhưng đầy cuốn hút cho trí não với thiết kế Material Design 3 cao cấp, hiệu ứng chuyển động mượt mà, âm thanh du dương và cảm giác rung xúc giác (haptics) tinh tế.

---

## 2. Các tính năng cốt lõi

### 🎮 3 Chế độ chơi đa dạng & cuốn hút
1. **Lumina Prism (Logic & Phản xạ tia sáng)**:
   - Xoay các gương phản chiếu và lăng kính đổi màu để dẫn các tia sáng từ nguồn đến điểm đích.
   - Hơn 25 màn chơi phân cấp từ dễ đến thử thách cùng chế độ tạo màn ngẫu nhiên vô tận.
2. **Zen Merge (Hợp nhất khối số mượt mà)**:
   - Thao tác vuốt nhạy bén, các ô số tối giản tinh tế kết hợp cùng hiệu ứng hòa trộn mượt mà.
   - Hỗ trợ Hoàn tác (Undo), combo điểm số và hiệu ứng vỡ hạt sao nhẹ nhàng.
3. **Echo Resonance (Ký ức & Nhịp điệu âm thanh)**:
   - Thử thách trí nhớ âm thanh và thị giác: các ô phát sáng kèm chuỗi hợp âm thư giãn, người chơi lặp lại chuỗi giai điệu ngày càng dài.

### 🎨 Thiết kế giao diện (UI/UX) Tối giản & Thanh lịch
- **Bảng màu cao cấp**: Nền tối sâu (Dark Obsidian) hoặc sáng thanh thoát (Off-white / Warm Gray) kết hợp với các dải màu pastel neon nhẹ nhàng (Mint, Lavender, Soft Amber).
- **Trải nghiệm xúc giác & Âm thanh độc bản**: 
  - Âm thanh hợp âm êm dịu tổng hợp trực tiếp (Resonant Chimes & Synthesizer), hoạt động mượt mà và tức thì.
  - Phản hồi rung haptic xúc giác cho từng cú chạm, xoay gương hoặc hợp nhất ô.
- **Tùy biến phong cách**: Cho phép người chơi chọn nhiều theme màu sắc tối giản (Midnight, Zen Bamboo, Sunset Glow, Nordic Frost).

### 🏆 Lưu trữ & Thống kê cá nhân (Room Database)
- Hệ thống cơ sở dữ liệu Room Database lưu trữ cục bộ:
  - Điểm cao nhất từng chế độ, kỷ lục chuỗi thắng (Streak).
  - Huy hiệu thành tựu (Zen Master, Light Weaver, Echo Memory).
  - Bảng thống kê tiến độ chơi và xếp hạng sao.

### 💡 Trợ lý câu đố thông minh (Smart Hint)
- Tính năng gợi ý thông minh giúp người chơi khi gặp các màn hóc búa mà không làm mất đi niềm vui tự khám phá.

---

## 3. Kiến trúc kỹ thuật & Thư viện
- **Ngôn ngữ & Nền tảng**: Kotlin 100%, Jetpack Compose (Material 3).
- **Cơ sở dữ liệu**: Android Jetpack Room (với KSP & Kotlin Coroutines/Flow).
- **Kiến trúc**: MVVM (Model-View-ViewModel) với StateFlow để quản lý trạng thái mượt mà không giật lag.
- **Audio & Haptics Engine**: Android AudioTrack / ToneGenerator & Vibrator API tạo trải nghiệm giác quan sống động.
- **Biểu tượng & Icon**: Adaptive Custom App Icon chuẩn Material phong cách tối giản hình khối.

---

## 4. Kế hoạch triển khai từng bước
1. **Bước 1**: Cấu hình Application ID, Metadata (`Zenith`), chuỗi tài nguyên tiếng Việt & tiếng Anh, thiết kế Launcher Icon tối giản.
2. **Bước 2**: Xây dựng Core Engine âm thanh (Audio Chimes) & Haptics.
3. **Bước 3**: Thiết lập Room Database cho Điểm số, Màn chơi và Thành tựu (Achievements).
4. **Bước 4**: Hiện thực hoá màn chơi chính và 3 trò chơi (Lumina Prism, Zen Merge, Echo Resonance) với Compose Canvas & Gesture Detection.
5. **Bước 5**: Hoàn thiện Menu chính thanh lịch, Bảng thống kê (Stats & Achievements), và cài đặt Theme.
6. **Bước 6**: Kiểm thử và biên dịch ứng dụng (`compile_applet`).
