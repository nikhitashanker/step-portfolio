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
  getComments();
  addListenersToButtons();
  showFirstTabContent();
};

function addListenersToButtons() {
  const expandableButtons = document.getElementsByClassName('expandable');

  // Add an event listener for click to each button.
  for (let buttonNum = 0; buttonNum < expandableButtons.length; buttonNum++) {
    expandableButtons[buttonNum].addEventListener(
        'click', toggleContentVisibility);
  }
}

function showFirstTabContent() {
  const tabContent = document.getElementsByClassName('tab-content');

  // Make the first tab shown.
  if (tabContent[0] !== undefined) {
    showContentForTab(tabContent[0].id, 'peachpuff');
  }
}

/**
 * Adds a random greeting to the page.
 */
function addRandomFact() {
  const randomFacts = [
    'I like to peel cuties in a way that keeps the peel in one piece.',
    'I really like kiwis.',
    'When I was younger, I wanted to be an astronaut.',
    'My favorite movie is Despicable Me 2.',
  ];

  // Pick a random fact.
  const fact = randomFacts[Math.floor(Math.random() * randomFacts.length)];

  // Add it to the page.
  const randomFactContainer = document.getElementById('random-fact-container');
  randomFactContainer.innerText = fact;
}

/*
 * Expands content when content title button is clicked.
 */
function toggleContentVisibility() {
  const content = this.nextElementSibling;

  // Toggle the visibility of the content.
  if (content.style.display === 'block') {
    content.style.display = 'none';
  } else {
    content.style.display = 'block';
  }

  // Toggle whether plus or minus is shown on the button.
  this.classList.toggle('active');
}

/*
 * Shows project content that corresponds to selected tab
 * and sets the tab color to the content background color.
 */
function showContentForTab(selectedProjectName, activeTabColor) {
  const tabContent = document.getElementsByClassName('tab-content');
  const tabButtons = document.getElementsByClassName('select-tab');

  // Make the only the tab content with id the same as the selected
  // project name visible and make only the corresponding tab match
  // with the content background color.
  for (let i = 0; i < tabContent.length; i++) {
    if (tabContent[i].id === selectedProjectName) {
      tabContent[i].style.display = 'inline-block';
      tabButtons[i].style.backgroundColor = activeTabColor;
    } else {
      tabContent[i].style.display = 'none';
      tabButtons[i].style.backgroundColor = 'aliceblue';
    }
  }
}

/*
 * Fetches the JSON string for comments from the server and display
 * the comments.
 */
function getComments() {
  const numberOfComments = document.getElementById('comments-limit').value;
  fetch(getQueryString(numberOfComments))
      .then((response) => response.json())
      .then((comments) => {
        const commentContainer = document.getElementById('comment-container');

        // Remove existing display.
        while (commentContainer.firstChild) {
          commentContainer.removeChild(commentContainer.firstChild);
        }

        // Build display of comments.
        comments.forEach((comment) => {
          commentContainer.appendChild(createDivElement(
              commentToString(comment), comment.blobKeyString));
        });
      });
}

/*
 * Deletes all comments and refreshes display.
 */
function deleteComments() {
  const request = new Request('/delete-data', {method: 'POST'});

  fetch(request).then(() => {
    getComments();
  });
}

/*
 * Creates an <div> element containing text and an image
 * if specified.
 */
function createDivElement(text, blobstoreKeyString) {
  const div = document.createElement('div');
  div.className = 'speech-bubble';

  const h4Element = document.createElement('h4');
  h4Element.innerText = text;
  div.appendChild(h4Element);

  // If this comment has an image, add it as a child of the div element.
  if (blobstoreKeyString !== undefined) {
    const imgElement = document.createElement('img');
    const request = new Request(
        getBlobStoreRequestString(blobstoreKeyString), {method: 'GET'});
    fetch(request).then((response) => response.blob()).then((blob) => {
      const objectUrl = URL.createObjectURL(blob);
      imgElement.src = objectUrl;
      div.append(imgElement);
    });
  }
  return div;
}

function getQueryString(numberOfComments) {
  return `/data?number-of-comments=${numberOfComments}`;
}

function commentToString(comment) {
  return `${comment.commenterName} (${comment.commenterEmail}) says \
        ${comment.text}`;
}

function getBlobStoreRequestString(blobstoreKeyString) {
  return `/blobstore-image?blob-key=${blobstoreKeyString}`;
}
