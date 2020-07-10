// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

window.onload = function onLoad() {
  fetchLoginUrl();
  fetchLogoutUrl();
  checkLoginStatus();
};

/*
 * Checks login status and shows comment form, greeting, and logout prompt if
 * user is logged in, and shows login prompt if user is not logged in.
 */
function checkLoginStatus() {
  fetch('/login-status')
      .then((response) => {
        return response.json();
      })
      .then((loginStatus) => {
        const isLoggedIn = loginStatus.isLoggedIn;
        showCommentForm(isLoggedIn);
        showLoginOrLogoutPrompt(isLoggedIn);
        showUserInfoFormAndGreeting(isLoggedIn);
      });
}

function showCommentForm(isLoggedIn) {
  const commentForm = document.getElementById('comment-form');
  if (isLoggedIn) {
    // Show comment form after image URL is fetched.
    fetch('/blobstore-upload-url')
        .then((response) => {
          return response.text();
        })
        .then((imageUploadUrl) => {
          const commentForm = document.getElementById('comment-form');
          commentForm.action = imageUploadUrl;
          commentForm.classList.remove('hidden');
        });
  } else {
    commentForm.classList.add('hidden');
  }
}

function showLoginOrLogoutPrompt(isLoggedIn) {
  const loginPrompt = document.getElementById('login-prompt');
  const logoutPrompt = document.getElementById('logout-prompt');
  if (isLoggedIn) {
    loginPrompt.classList.add('hidden');
    logoutPrompt.classList.remove('hidden');
  } else {
    loginPrompt.classList.remove('hidden');
    logoutPrompt.classList.add('hidden');
  }
}

function fetchLoginUrl() {
  fetch('/login-url')
      .then((response) => {
        return response.text();
      })
      .then((loginUrl) => {
        const loginPrompt = document.getElementById('login-prompt');
        loginPrompt.href = loginUrl;
      });
}

function fetchLogoutUrl() {
  fetch('/logout-url')
      .then((response) => {
        return response.text();
      })
      .then((logoutUrl) => {
        const logoutPrompt = document.getElementById('logout-prompt');
        logoutPrompt.href = logoutUrl;
      });
}

function showUserInfoFormAndGreeting(isLoggedIn) {
  const greeting = document.getElementById('greeting');
  const userInfoForm = document.getElementById('user-info-form');
  if (isLoggedIn) {
    fetch('/user-info')
        .then((response) => {
          return response.json();
        })
        .then((userInfo) => {
          if (userInfo) {
            const username = document.getElementById('username');
            username.value = userInfo.username;
            const showEmail = document.getElementById('show-email');
            showEmail.checked = userInfo.showEmail;
            greeting.innerText = getGreeting(userInfo.email);
          }
        });
    userInfoForm.classList.remove('hidden');
  } else {
    greeting.innerText = 'Hello there!';
    userInfoForm.classList.add('hidden');
  }
}

function getGreeting(email) {
  return `Hi there! You are currently signed in as ${email}.`;
}
