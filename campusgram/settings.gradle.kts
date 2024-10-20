rootProject.name = "campusgram"
include("article")
include("article:presentation")
findProject(":article:presentation")?.name = "presentation"
include("article:core")
findProject(":article:core")?.name = "core"
include("article:persistence")
findProject(":article:persistence")?.name = "persistence"
include("image")
include("user")
include("gateway")
