from fastapi import FastAPI
import routers.register
import routers.verify_email
import routers.login
import routers.users
import routers.search_projects
import routers.projects
import routers.tags
import routers.recommend
import routers.applications

app = FastAPI()


app.include_router(routers.register.router)
app.include_router(routers.verify_email.router)
app.include_router(routers.login.router)
app.include_router(routers.users.router)
app.include_router(routers.search_projects.router)
app.include_router(routers.projects.router)
app.include_router(routers.tags.router)
app.include_router(routers.recommend.router)
app.include_router(routers.applications.router)
