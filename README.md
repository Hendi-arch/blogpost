
# Blog Application
This is a simple RESTful API for a blog application built with Spring Boot and Spring Data JPA. The API allows users to create, retrieve, update, and delete blog posts.

## Prerequisites
- Java JDK 17 or higher
- Apache Maven

## Run Locally
Clone the project

```bash
  git clone git clone https://github.com/Hendi-arch/blogpost.git
```

Go to the project directory

```bash
  cd blogpost
```

Build the application

```bash
  mvn clean install
```

Run the application

```bash
  mvn spring-boot:run
```

The application will start running on **http://localhost:2222**

## API Documentation
The API documentation is available in the API Specification file `api-spec.yaml` or you can visit **[Swagger](https://app.swaggerhub.com/apis/HENDINOF22/blog-api/1.1.0)**.

## How To Use
1. Create a blog post
Send a POST request to `/api/blog-posts/createBlogPosts` with the following JSON payload:
```json
{
  "title": "Example Title",
  "body": "Example Body",
  "author": "John Doe"
}
```

2. Retrieve a single blog post
Send a GET request to `/api/blog-posts/{id}` where `{id}` is the ID of the blog post.

<!--
@RequestParam(defaultValue = "0") int page,
@RequestParam(defaultValue = "25") int size,
@RequestParam(defaultValue = "id:asc", name = "sort_by") String[] sort
-->

3. Retrieve a list of all blog posts
Send a GET request to `/api/blog-posts/findAllBlogPosts?page=0&size=25&sort_by=id:desc`.

4. Update a blog post
Send a PUT request to `/api/blog-posts/{id}` where `{id}` is the ID of the blog post, with the following JSON payload:
```json
{
  "title": "Updated Title",
  "body": "Updated Body",
  "author": "Jane Doe"
}
```

5. Delete a blog post
Send a DELETE request to `/api/blog-posts/{id}` where `{id}` is the ID of the blog post.

## Tech Stack
- Spring Boot
- Spring Data JPA
- H2 Database (in-memory)
