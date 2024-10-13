# 앱 구조

Kakao Mobility의 API를 사용하여 특정 위치에 대한 경로, 거리 및 시간을 검색하는 기능 제공
앱은 Android Jetpack Compose를 사용하여 UI를 구축하며, Hilt를 통해 의존성 주입을 관리

# 주요 구성 요소

Architecture: MVVM (Model-View-ViewModel) 패턴
UI: Jetpack Compose를 사용하여 반응형 UI를 구축
Dependency Injection: Dagger Hilt를 사용하여 의존성을 주입
Networking: Retrofit 라이브러리를 사용하여 Kakao Mobility API와 통신

# 주요 구현 사항

API 통신: KakaoMobilityApiService를 통해 Kakao Mobility의 API와 통신
- getLocationsNameList(): 출발지,목적지 목록들을 가져오는 메서드.
- getLocationPath(): 시작점과 도착점 간의 경로 및 도로상태를 가져오는 메서드.
- getLocationTimeDistance(): 시작점과 도착점 간의 거리 및 시간 가져오는 메서드.

UI 구성: Jetpack Compose를 사용하여 사용자 인터페이스 설계
- 로딩 상태를 표시하기 위해 프로그레스 바를 사용
- 출발지 및 도착지 리스트를 받을 수 있는 UI 구성 요소 제공

# 디렉토리 구조

- com.example.kakomobilityassignment
    - data
        - api
            - ApiComponents
                - Location.kt
                - LocationListResponse.kt
                - LocationPath.kt
                - LocationTimeDistanceResponse.kt // API 데이터 클래스
            - KakaoMobilityApiService.kt // API 인터페이스
        - repository
            - KakaoMobilityRepository.kt // API 호출 및 데이터 처리
    - di
        - NetworkModule.kt // Kakao Mobility Api 객체 생성 및 주입 모듈
    - presentation
        - KakaoMobilityApplication.kt
        - AssignLatLng.kt
        - MainActivity.kt
        - NavigationViewController.kt
        - KakaoMobilityScreenTemplate.kt
        - common
            - CommonComponents.kt // 뷰 전체에 공통적으로 사용되는 Compose 메소드들
        - viewModel
            - LocationListViewModel.kt
            - PathViewModel.kt

