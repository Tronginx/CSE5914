import os
import discord
import replicate
from discord import Intents
from discord.ext import commands
from dotenv import load_dotenv
from keytotext import pipeline
import twitter
import bestGuess

import responses

load_dotenv()


async def send_message(message, user_message, is_private):
    try:
        response = responses.handle_response(user_message)
        await message.author.send(response) if is_private else await message.channel.send(response)
    except Exception as e:
        print(e)


def run_discord_bot():
    TOKEN = os.getenv('DISCORD_TOKEN')
    intents = Intents.default()
    intents.message_content = True
    client = commands.Bot(
        command_prefix="!",
        description="Runs models on Replicate!",
        intents=intents,
    )

    @client.event
    async def on_ready():
        print(f'{client.user} is now running!')

    @client.command()
    async def hello(ctx):
        """Formal greeting!"""
        await ctx.send("Hello, I'm the Grabble bot!")

    @client.command()
    async def bye(ctx):
        """Formal goodbye!"""
        await ctx.send("See ya!")

    @client.command()
    async def help_me(ctx):
        """Instruction on how to use the main function"""
        await ctx.send('Type in different words, and then I\'ll create a sentence to draw a picture for you. '
                       '\nUse the command "Dream something"')

    @client.command()
    async def instruction(ctx):
        """Instruction set"""
        await ctx.send('instructions: [hello, bye, dream something, have something (format: !have a, b, "c d")'
                       ', help_me, instruction, scrape]')

    @client.command()
    async def dream(ctx, *, prompt):
        """Generate an image from a text prompt using the stable-diffusion model"""
        msg = await ctx.send(f"“{prompt}”\n> Generating...")
        model = replicate.models.get("stability-ai/stable-diffusion")
        image = model.predict(prompt=prompt)[0]
        await msg.edit(content=f"“{prompt}”\n{image}")

    @client.command()
    async def scrape(ctx, username, path):
        """Scrape the twitter account images"""
        images = twitter.get_twitter(username, path)
        await ctx.send(f'{username}\'s recent 10 images are collected')
        await ctx.send(f'You may find the images at: {path}')
        for image in images:
            await ctx.send(f'{image}')


    @client.command()
    async def have(ctx, *args):
        """Generate a sentence based on the given arguments"""
        arguments = ', '.join(args)
        nlp = pipeline("k2t")
        input_args = []
        for arg in args:
            input_args.append(arg)
        await ctx.send(f'{len(args)} arguments: {arguments}')
        await ctx.send(f'The generated sentence is: {nlp(input_args)}')

    @client.command()
    async def keyword(ctx, *args):
        """Generate keywords based on the given images"""
        paths = []
        for arg in args:
            paths.append(arg)
        keywords = bestGuess.detect_web(paths)
        for word in keywords:
            await ctx.send(f'Best guess label: {word}')


    @client.event
    async def on_member_join(member):
        channel = client.get_channel(1046883002577322077)
        await channel.send("Hey there, welcome to the channel!")

    @client.event
    async def on_message(message):
        if message.author == client.user:
            return

        username = str(message.author)
        user_message = str(message.content)
        channel = str(message.channel)

        print(f"{username} said: '{user_message}' in '{channel}'")

        if len(user_message) == 0:
            print("No input")
            return

        if user_message[0] == '!':
            await client.process_commands(message)
            return

        if user_message[0] == '?':
            user_message = user_message[1:]
            await send_message(message, user_message, is_private=True)
        else:
            await send_message(message, user_message, is_private=False)

    client.run(TOKEN)
