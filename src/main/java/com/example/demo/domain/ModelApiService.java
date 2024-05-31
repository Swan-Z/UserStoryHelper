package com.example.demo.domain;
import com.example.demo.domain.AskRequest;
import com.example.demo.domain.GPTResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ModelApiService {

    private final RestTemplate restTemplate;

    @Autowired
    public ModelApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public GPTResponse getDataFromApi(AskRequest askRequest) {
        String url = "https://iop-isai-producto-dqa-dev.cloud.inditex.com/api/ask";
        String cookieValue = "_clck=w6iyev%7C2%7Cfm7%7C0%7C1517; _abck=A3D57E53F5AD82B0A93AA6F003C5BE02~0~YAAQFTYTAmduB6uPAQAA16VjygvwC/ndulE1owOYXcyuZe8INa5ybEUS49jb4za7UFyDosofap0xqGf7KtvvyFyL81ixUyBlG02L1w4s3SnUyEqKF4KbYPAJzNxelA8zvXndgb0LplY8ugdpJBlXUyCYtXHYsvPQZrcxVcmocve35SLpLK/K2NhDNVmRM0l1KXBjXDfn/KvOE0p3JPWHnJpb49CarA7POZBnfbqyy0S6PWYzsgFik9Xw2lxnXhNGfye4ydrSHQSAmuJaNQec/D8TgWLMa9iyiRaL55QzP+E6GlvqjncSkLVDRdPYrvekVTG9HQTBo9C3UGgPqEFxddtWl8DBgHAaj0p7kI9NHMT6muCds8/CHmI07GIVmhqK/6Ry+aJcStVnx9R9QEa3M7vtUXRBuqQimA==~-1~||0||~-1; _clsk=1ba27qd%7C1717087481207%7C1%7C1%7Ch.clarity.ms%2Fcollect; AppServiceAuthSession=nJImuPNLY7mYLREGSD8MsLF222AcZUidaEpoEOVyFFmVUWzuq0qOPtO8FkDVhkJ2MooQeqkM0etxX97SSb9WRnfoUgej7kOwQ04fOlfMrYuTlO9lJRITckJTMYwp8mOWgfWEoTpPhOxUjfKwFtVTOBiqV4TGXOj6k2bU65mmFzH+5EkTyLOR8penI+zqDCuTz9QZrM49zjwxnGJ4EzcwfR0EHrAh2d402FrSULt+97Vqx5GJK0w8e9ZBA62tWYYjQXRcwCaXJ4yD4VzuLr5sf/eOsLr9f1kSmQVwjPTTuAR6BRamO6uLZQZMDkASssJKXgFZgmUizF9SMTJxFMmwabh7axrlySPYjC4CebYf/2XkVvhZd5FRUOTCS3+grof1Iy7eshh0SUJSDEgWXbo8WbibYYn8dTXatMcgm4nYg+S0mqPstiExVnCympirU9MT9awVtBSHMebAHLXbk53lsU023Aj/sKfiCkQ/51aCSek0w7xaySMceF4Fbfr8quJpT/FBJBU5MUBuzCRRktHzGAx+7F77QgEZGF0tOgjR4DE87oBZNBZE8FlEKAwnAqW6qP2KFyZq1FYdAgxcNyEAnu5yvXKpcbohPmK8jkWafb3OXgxS2RCmQZFmoX29B12DthkUFxusZ2hY6isKZRFmhtnz5tqoBesCm4GhKaCP1p+JiBDJX/IQ/uUHMYeyiiirgJ9tNb6jO1HFxkWjmqSCcMlMO08OcqQF9Q+0KMUZYvhF574fmWJKT3fP6lCf5mXWsOcYuxct1rer7VTWajxYU2W9JDzQ9mKwzd1BJBEdym4A5baIXSTCU0FpfIm1sYXKY4GAfZYd+sXpf6JsX6ZyfLOkAkDqTXy5ZBn5lQq6rPKvgXccdNQuhqGC89m/rRZ50B8T33br+Bt5EhOrYArYYJutHE8ZjtFRz9T0JsmAya7FSRTpHuLfKgs8lThtuSaq6+I4RaDa6Wqk3ViEF8gfRg==";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", cookieValue);

        HttpEntity<AskRequest> requestEntity = new HttpEntity<>(askRequest, headers);

        return this.restTemplate.postForObject(url, requestEntity, GPTResponse.class);
    }


}


